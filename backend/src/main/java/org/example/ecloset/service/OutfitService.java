package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.OutfitCreateRequest;
import org.example.ecloset.dto.request.OutfitUpdateRequest;
import org.example.ecloset.dto.response.OutfitResponseDto;
import org.example.ecloset.entity.Item;
import org.example.ecloset.entity.Outfit;
import org.example.ecloset.entity.Season;
import org.example.ecloset.entity.User;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;
import org.example.ecloset.exception.ItemNotFoundException;
import org.example.ecloset.exception.OutfitNotFoundException;
import org.example.ecloset.exception.SeasonNotFoundException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.mapper.OutfitMapper;
import org.example.ecloset.repository.ItemRepository;
import org.example.ecloset.repository.OutfitRepository;
import org.example.ecloset.repository.SeasonRepository;
import org.example.ecloset.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ItemRepository itemRepository;
    private final OutfitMapper outfitMapper;

    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getAllOutfitsByUser(Integer userId) {
        return outfitRepository.findByUser_UserId(userId).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OutfitResponseDto getOutfitById(Integer userId, Integer outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден"));
        checkOwnership(outfit, userId);
        return outfitMapper.toResponseDto(outfit);
    }

    @Transactional
    public OutfitResponseDto createOutfit(Integer userId, OutfitCreateRequest request, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Season season = getSeasonIfPresent(request.getSeasonId());
        Set<Item> items = loadAndCheckItems(request.getItemIds(), userId);

        String imageUrl = fileStorageService.store(image);

        try {
            Outfit outfit = outfitMapper.toEntity(request, user, season);
            outfit.setImageUrl(imageUrl);
            outfit.setItems(items);

            Outfit savedOutfit = outfitRepository.save(outfit);
            return outfitMapper.toResponseDto(savedOutfit);
        } catch (RuntimeException e) {
            // откатываем файл
            fileStorageService.delete(imageUrl);
            throw e;
        }
    }

    @Transactional
    public OutfitResponseDto updateOutfit(Integer userId, Integer outfitId,
                                          OutfitUpdateRequest request, MultipartFile image) {
        Outfit existingOutfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден"));
        checkOwnership(existingOutfit, userId);

        Season season = getSeasonIfPresent(request.getSeasonId());
        Set<Item> items = loadAndCheckItems(request.getItemIds(), userId);

        String oldImageUrl = existingOutfit.getImageUrl();
        String newImageUrl = null;

        if (image != null && !image.isEmpty()) {
            newImageUrl = fileStorageService.store(image);
        }

        try {
            outfitMapper.updateEntity(request, existingOutfit, season);
            existingOutfit.setItems(items);

            if (newImageUrl != null) {
                existingOutfit.setImageUrl(newImageUrl);
            }

            Outfit savedOutfit = outfitRepository.save(existingOutfit);

            // после успешного сохранения в БД удаляем старый файл
            if (newImageUrl != null) {
                fileStorageService.delete(oldImageUrl);
            }

            return outfitMapper.toResponseDto(savedOutfit);
        } catch (RuntimeException e) {
            // откат
            if (newImageUrl != null) {
                fileStorageService.delete(newImageUrl);
            }
            throw e;
        }
    }

    @Transactional
    public void deleteOutfit(Integer userId, Integer outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден"));
        checkOwnership(outfit, userId);

        String imageUrl = outfit.getImageUrl();

        outfitRepository.delete(outfit);

        fileStorageService.delete(imageUrl);
    }

    @Transactional
    public OutfitResponseDto wearOutfit(Integer userId, Integer outfitId) {
        int updatedRows = outfitRepository.incrementTimesWorn(outfitId, userId);
        if (updatedRows == 0) {
            throw new OutfitNotFoundException("Аутфит не найден или у вас нет прав на это действие");
        }

        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден после обновления"));

        for (Item item : outfit.getItems()) {
            itemRepository.incrementTimesWorn(item.getItemId(), userId);
        }

        return outfitMapper.toResponseDto(outfit);
    }


    @Transactional
    public OutfitResponseDto addItemToOutfit(Integer userId, Integer outfitId, Integer itemId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден"));
        checkOwnership(outfit, userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        checkItemOwnership(item, userId);

        outfit.getItems().add(item);

        return outfitMapper.toResponseDto(outfit);
    }

    @Transactional
    public OutfitResponseDto replaceItemInOutfit(Integer userId, Integer outfitId,
                                                 Integer oldItemId, Integer newItemId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("Аутфит не найден"));
        checkOwnership(outfit, userId);

        Item oldItem = outfit.getItems().stream()
                .filter(i -> i.getItemId().equals(oldItemId))
                .findFirst()
                .orElse(null);

        if (oldItem != null) {
            outfit.getItems().remove(oldItem);
        }

        Item newItem = itemRepository.findById(newItemId)
                .orElseThrow(() -> new ItemNotFoundException("Новая вещь не найдена"));
        checkItemOwnership(newItem, userId);

        outfit.getItems().add(newItem);
        unlockOutfitIfUnavailable(outfit);

        return outfitMapper.toResponseDto(outfit);
    }

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getOutfitsByTemperature(Integer userId, Integer temperature) {
        List<Season> suitableSeasons = seasonRepository.findSeasonsByTemperature(temperature);
        if (suitableSeasons.isEmpty()) {
            return List.of();
        }
        return outfitRepository.findByUser_UserIdAndSeasonIn(userId, suitableSeasons).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getOutfitsByTimesWorn(Integer userId, Integer timesWorn) {
        return outfitRepository.findByUser_UserIdAndTimesWorn(userId, timesWorn).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getOutfitsByColor(Integer userId, Color color) {
        return outfitRepository.findByUser_UserIdAndColor(userId, color).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getOutfitsByOccasion(Integer userId, Occasion occasion) {
        return outfitRepository.findByUser_UserIdAndOccasion(userId, occasion).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OutfitResponseDto> getUnavailableOutfits(Integer userId) {
        return outfitRepository.findByUser_UserIdAndUnavailableTrue(userId).stream()
                .map(outfitMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    private void unlockOutfitIfUnavailable(Outfit outfit) {
        if (Boolean.TRUE.equals(outfit.getUnavailable())) {
            outfit.setUnavailable(false);
        }
    }

    private Season getSeasonIfPresent(Integer seasonId) {
        if (seasonId == null) {
            return null;
        }
        return seasonRepository.findById(seasonId)
                .orElseThrow(() -> new SeasonNotFoundException("Сезон с ID " + seasonId + " не найден"));
    }

    private Set<Item> loadAndCheckItems(List<Integer> itemIds, Integer userId) {
        Set<Item> items = new HashSet<>();
        for (Integer itemId : itemIds) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException("Вещь с ID " + itemId + " не найдена"));
            checkItemOwnership(item, userId);
            items.add(item);
        }
        return items;
    }

    private void checkOwnership(Outfit outfit, Integer userId) {
        if (!outfit.getUser().getUserId().equals(userId)) {
            throw new OutfitNotFoundException("Аутфит не найден или не принадлежит вам");
        }
    }

    private void checkItemOwnership(Item item, Integer userId) {
        if (!item.getUser().getUserId().equals(userId)) {
            throw new ItemNotFoundException("Вещь не найдена или не принадлежит вам");
        }
    }
}