package org.example.ecloset.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.OutfitCreateRequest;
import org.example.ecloset.dto.request.OutfitUpdateRequest;
import org.example.ecloset.dto.response.OutfitResponseDto;
import org.example.ecloset.entity.Outfit;
import org.example.ecloset.entity.Season;
import org.example.ecloset.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OutfitMapper {

    private final SeasonMapper seasonMapper;
    private final ItemMapper itemMapper;

    public Outfit toEntity(OutfitCreateRequest request, User user, Season season) {
        return Outfit.builder()
                .user(user)
                .season(season)
                .color(request.getColor())
                .occasion(request.getOccasion())
                .build();
    }

    public void updateEntity(OutfitUpdateRequest request, Outfit existingOutfit, Season season) {
        existingOutfit.setSeason(season);
        existingOutfit.setColor(request.getColor());
        existingOutfit.setOccasion(request.getOccasion());
        existingOutfit.setUnavailable(false);
    }

    public OutfitResponseDto toResponseDto(Outfit outfit) {
        if (outfit == null) {
            return null;
        }
        return OutfitResponseDto.builder()
                .outfitId(outfit.getOutfitId())
                .season(seasonMapper.toDto(outfit.getSeason()))
                .color(outfit.getColor())
                .occasion(outfit.getOccasion())
                .dateAdded(outfit.getDateAdded())
                .timesWorn(outfit.getTimesWorn())
                .unavailable(outfit.getUnavailable())
                .imageUrl(outfit.getImageUrl())
                .items(outfit.getItems().stream()
                        .map(itemMapper::toResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }
}