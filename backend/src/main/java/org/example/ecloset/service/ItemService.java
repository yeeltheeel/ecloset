package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ItemCreateRequest;
import org.example.ecloset.dto.request.ItemUpdateRequest;
import org.example.ecloset.dto.response.ItemResponseDto;
import org.example.ecloset.entity.Item;
import org.example.ecloset.entity.Season;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.AccessDeniedException;
import org.example.ecloset.exception.ItemNotFoundException;
import org.example.ecloset.exception.SeasonNotFoundException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.mapper.ItemMapper;
import org.example.ecloset.repository.ItemRepository;
import org.example.ecloset.repository.SeasonRepository;
import org.example.ecloset.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ItemMapper itemMapper;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsByUser(Integer userId) {
        return itemRepository.findByUser_UserId(userId).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponseDto createItem(Integer userId, ItemCreateRequest request, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Season season = null;
        if (request.getSeasonId() != null) {
            season = seasonRepository.findById(request.getSeasonId())
                    .orElseThrow(() -> new SeasonNotFoundException("Сезон с ID " + request.getSeasonId() + " не найден"));
        }

        // Сначала сохраняем файл — если что-то упадёт, в БД ничего не попадёт
        String imageUrl = fileStorageService.store(image);

        try {
            Item item = itemMapper.toEntity(request, user, season);
            item.setImageUrl(imageUrl);
            Item savedItem = itemRepository.save(item);
            return itemMapper.toResponseDto(savedItem);
        } catch (RuntimeException e) {
            // откатываем файл, если БД упала
            fileStorageService.delete(imageUrl);
            throw e;
        }
    }

    @Transactional
    public ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateRequest request, MultipartFile image) {
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

        // Если пришла новая картинка — сохраняем её
        if (image != null && !image.isEmpty()) {
            newImageUrl = fileStorageService.store(image);
        }

        try {
            Item updatedItem = itemMapper.updateEntity(request, existingItem, season);
            if (newImageUrl != null) {
                updatedItem.setImageUrl(newImageUrl);
            }
            Item savedItem = itemRepository.save(updatedItem);

            // Только после успешного сохранения в БД удаляем старый файл
            if (newImageUrl != null) {
                fileStorageService.delete(oldImageUrl);
            }

            return itemMapper.toResponseDto(savedItem);
        } catch (RuntimeException e) {
            // если БД упала — удаляем только что сохранённый новый файл
            if (newImageUrl != null) {
                fileStorageService.delete(newImageUrl);
            }
            throw e;
        }
    }

    @Transactional
    public void deleteItem(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));

        checkOwnership(item, userId);

        String imageUrl = item.getImageUrl();
        itemRepository.delete(item);

        // Удаляем файл после успешного удаления из БД.
        // Если файл не удалится — БД уже консистентна, просто останется мусор.
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

    private void checkOwnership(Item item, Integer userId) {
        if (!item.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("У вас нет прав на это действие");
        }
    }
}