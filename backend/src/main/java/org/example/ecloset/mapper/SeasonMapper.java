package org.example.ecloset.mapper;

import org.example.ecloset.dto.response.SeasonDto;
import org.example.ecloset.entity.Season;
import org.springframework.stereotype.Component;

@Component
public class SeasonMapper {

    // Season -> SeasonDto
    public SeasonDto toDto(Season season) {
        if (season == null) {
            return null;
        }
        return SeasonDto.builder()
                .seasonId(season.getSeasonId())
                .name(season.getName())
                .minTemp(season.getMinTemp())
                .maxTemp(season.getMaxTemp())
                .build();
    }
}