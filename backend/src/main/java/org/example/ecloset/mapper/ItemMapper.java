package org.example.ecloset.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ItemCreateRequest;
import org.example.ecloset.dto.request.ItemUpdateRequest;
import org.example.ecloset.dto.response.ItemResponseDto;
import org.example.ecloset.entity.Item;
import org.example.ecloset.entity.Season;
import org.example.ecloset.entity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final SeasonMapper seasonMapper;

    // Для создания (PUT/POST)
    public Item toEntity(ItemCreateRequest request, User user, Season season) {
        return Item.builder()
                .user(user)
                .category(request.getCategory())
                .season(season)
                .color(request.getColor())
                .material(request.getMaterial())
                .occasion(request.getOccasion())
                .brand(request.getBrand())
                .price(request.getPrice())
                .timesWorn(request.getInitialTimesWorn() != null ? request.getInitialTimesWorn() : 0)
                .build();
    }

    // Для обновления (PUT) - полная перезапись полей
    public Item updateEntity(ItemUpdateRequest request, Item existingItem, Season season) {
        existingItem.setCategory(request.getCategory());
        existingItem.setSeason(season);
        existingItem.setColor(request.getColor());
        existingItem.setMaterial(request.getMaterial());
        existingItem.setOccasion(request.getOccasion());
        existingItem.setBrand(request.getBrand());
        existingItem.setPrice(request.getPrice());

        return existingItem;
    }

    // Для чтения
    public ItemResponseDto toResponseDto(Item item) {
        if (item == null) {
            return null;
        }
        return ItemResponseDto.builder()
                .itemId(item.getItemId())
                .category(item.getCategory())
                .season(seasonMapper.toDto(item.getSeason()))
                .color(item.getColor())
                .material(item.getMaterial())
                .occasion(item.getOccasion())
                .dateAdded(item.getDateAdded())
                .timesWorn(item.getTimesWorn())
                .imageUrl(item.getImageUrl())
                .brand(item.getBrand())
                .price(item.getPrice())
                .build();
    }
}