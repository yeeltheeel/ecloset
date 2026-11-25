package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.ai.AiAnalysisResult;
import org.example.ecloset.dto.request.ItemCreateRequest;
import org.example.ecloset.dto.request.ItemUpdateRequest;
import org.example.ecloset.dto.response.ItemAnalysisResponse;
import org.example.ecloset.dto.response.ItemResponseDto;
import org.example.ecloset.entity.Item;
import org.example.ecloset.entity.Outfit;
import org.example.ecloset.entity.Season;
import org.example.ecloset.entity.User;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;
import org.example.ecloset.exception.AiProcessingException;
import org.example.ecloset.exception.ItemNotFoundException;
import org.example.ecloset.exception.SeasonNotFoundException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.mapper.ColorMapper;
import org.example.ecloset.mapper.ItemMapper;
import org.example.ecloset.repository.ItemRepository;
import org.example.ecloset.repository.OutfitRepository;
import org.example.ecloset.repository.SeasonRepository;
import org.example.ecloset.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final OutfitRepository outfitRepository;
    private final ItemMapper itemMapper;
    private final FileStorageService fileStorageService;
    private final AiService aiService;
    private final ColorMapper colorMapper;

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsByUser(Integer userId) {
        return itemRepository.findByUser_UserId(userId).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponseDto createItem(Integer userId, ItemCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Season season = null;
        if (request.getSeasonId() != null) {
            season = seasonRepository.findById(request.getSeasonId())
                    .orElseThrow(() -> new SeasonNotFoundException("Сезон с ID " + request.getSeasonId() + " не найден"));
        }

        // Файл уже сохранён на этапе /analyze — только валидируем ссылку.
        String imageUrl = fileStorageService.requireStoredFile(request.getImageUrl());

        Item item = itemMapper.toEntity(request, user, season);
        item.setImageUrl(imageUrl);
        return itemMapper.toResponseDto(itemRepository.save(item));
    }

    @Transactional
    public ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateRequest request) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        checkOwnership(existingItem, userId);

        Season season = null;
        if (request.getSeasonId() != null) {
            season = seasonRepository.findById(request.getSeasonId())
                    .orElseThrow(() -> new SeasonNotFoundException("Сезон с ID " + request.getSeasonId() + " не найден"));
        }

        String oldImageUrl = existingItem.getImageUrl();
        String newImageUrl = null;

        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            String candidate = fileStorageService.requireStoredFile(request.getImageUrl());
            if (!candidate.equals(oldImageUrl)) {
                newImageUrl = candidate;
            }
        }

        // Маппер не трогает imageUrl — меняем фото только если пришёл новый URL.
        itemMapper.updateEntity(request, existingItem, season);
        if (newImageUrl != null) {
            existingItem.setImageUrl(newImageUrl);
        }

        Item savedItem = itemRepository.save(existingItem);

        // Старый файл удаляем только после успешного сохранения.
        if (newImageUrl != null) {
            fileStorageService.delete(oldImageUrl);
        }

        return itemMapper.toResponseDto(savedItem);
    }

    @Transactional
    public void deleteItem(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));

        checkOwnership(item, userId);

        // 1. Находим все аутфиты, в которых есть эта вещь.
        List<Outfit> affectedOutfits = outfitRepository.findByItems_ItemId(itemId);

        // 2. Помечаем их как недоступные.
        for (Outfit outfit : affectedOutfits) {
            outfit.setUnavailable(true);
        }
        if (!affectedOutfits.isEmpty()) {
            outfitRepository.saveAll(affectedOutfits);
        }

        String imageUrl = item.getImageUrl();

        // 3. Удаляем вещь.
        itemRepository.delete(item);

        // Удаляем файл после успешного удаления из БД.
        fileStorageService.delete(imageUrl);
    }

    @Transactional
    public ItemResponseDto wearItem(Integer userId, Integer itemId) {
        int updatedRows = itemRepository.incrementTimesWorn(itemId, userId);
        if (updatedRows == 0) {
            throw new ItemNotFoundException("Вещь не найдена или у вас нет прав на это действие");
        }

        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена после обновления"));

        return itemMapper.toResponseDto(updatedItem);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByTemperature(Integer userId, Integer temperature) {
        List<Season> suitableSeasons = seasonRepository.findSeasonsByTemperature(temperature);
        if (suitableSeasons.isEmpty()) {
            return List.of();
        }
        return itemRepository.findByUser_UserIdAndSeasonIn(userId, suitableSeasons).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        checkOwnership(item, userId);
        return itemMapper.toResponseDto(item);
    }

    // --- Методы поиска вещей ---

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByTimesWorn(Integer userId, Integer timesWorn) {
        return itemRepository.findByUser_UserIdAndTimesWorn(userId, timesWorn).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByCategory(Integer userId, Category category) {
        return itemRepository.findByUser_UserIdAndCategory(userId, category).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByColor(Integer userId, Color color) {
        return itemRepository.findByUser_UserIdAndColor(userId, color).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByMaterial(Integer userId, Material material) {
        return itemRepository.findByUser_UserIdAndMaterial(userId, material).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByOccasion(Integer userId, Occasion occasion) {
        return itemRepository.findByUser_UserIdAndOccasion(userId, occasion).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private void checkOwnership(Item item, Integer userId) {
        if (!item.getUser().getUserId().equals(userId)) {
            throw new ItemNotFoundException("Вещь не найдена или не принадлежит вам");
        }
    }

    /**
     * Первый этап создания вещи: анализ фото.
     * Сохраняется ТОЛЬКО обработанное изображение (PNG без фона), оригинал не хранится.
     * В БД ничего не пишется, поэтому @Transactional не нужен.
     */
    public ItemAnalysisResponse analyzeImage(MultipartFile image) {
        // 1. Дёргаем Python (ошибки 422/503 маппятся в AiService).
        AiAnalysisResult aiResult = aiService.analyze(image);

        if (aiResult.getProcessedImageBase64() == null || aiResult.getProcessedImageBase64().isBlank()) {
            throw new AiProcessingException("AI-сервис не вернул обработанное изображение");
        }

        // 2. Декодируем PNG.
        byte[] processedBytes;
        try {
            processedBytes = Base64.getDecoder().decode(aiResult.getProcessedImageBase64());
        } catch (IllegalArgumentException e) {
            throw new AiProcessingException("AI-сервис вернул некорректное изображение");
        }

        // 3. Категория: "t-shirt" → T_SHIRT.
        Category predictedCategory = null;
        if (aiResult.getCategory() != null) {
            try {
                predictedCategory = Category.valueOf(
                        aiResult.getCategory()
                                .toUpperCase()
                                .replace('-', '_')
                                .replace(' ', '_')
                );
            } catch (IllegalArgumentException ignored) {
                // не маппится — оставляем null
            }
        }

        // 4. Цвета.
        List<Color> colors = aiResult.getColorsHex() == null
                ? List.of()
                : aiResult.getColorsHex().stream()
                .map(colorMapper::fromHex)
                .filter(Objects::nonNull)
                .distinct()
                .limit(3)
                .toList();

        // 5. Сохраняем файл ПОСЛЕДНИМ: если что-то выше упало — на диске ничего не осталось.
        String processedUrl = fileStorageService.storeBytes(processedBytes, ".png");

        return ItemAnalysisResponse.builder()
                .imageUrl(processedUrl)
                .predictedCategory(predictedCategory)
                .categoryConfidence(aiResult.getCategoryConfidence())
                .predictedColors(colors)
                .rawColors(aiResult.getColorsHex())
                .build();
    }
}