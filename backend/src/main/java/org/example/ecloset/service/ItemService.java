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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ItemMapper itemMapper;

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

        Item item = itemMapper.toEntity(request, user, season);
        Item savedItem = itemRepository.save(item);

        return itemMapper.toResponseDto(savedItem);
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

        Item updatedItem = itemMapper.updateEntity(request, existingItem, season);
        Item savedItem = itemRepository.save(updatedItem);

        return itemMapper.toResponseDto(savedItem);
    }

    @Transactional
    public void deleteItem(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));

        checkOwnership(item, userId);
        itemRepository.delete(item);
    }

    @Transactional
    public ItemResponseDto wearItem(Integer userId, Integer itemId) {
        // Атомарный инкремент. Если вернёт 0, значит вещь не найдена или не принадлежит пользователю
        int updatedRows = itemRepository.incrementTimesWorn(itemId, userId);
        if (updatedRows == 0) {
            throw new ItemNotFoundException("Вещь не найдена или у вас нет прав на это действие");
        }

        // Перечитываем обновлённую сущность, чтобы вернуть актуальный timesWorn в DTO
        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена после обновления"));

        return itemMapper.toResponseDto(updatedItem);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsByTemperature(Integer userId, Integer temperature) {
        List<Season> suitableSeasons = seasonRepository.findSeasonsByTemperature(temperature);
        if (suitableSeasons.isEmpty()) {
            return List.of(); // Нет сезонов для такой температуры
        }
        return itemRepository.findByUser_UserIdAndSeasonIn(userId, suitableSeasons).stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Приватный хелпер для проверки прав
    private void checkOwnership(Item item, Integer userId) {
        if (!item.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("У вас нет прав на это действие");
        }
    }
}