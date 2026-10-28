package org.example.ecloset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutfitResponseDto {

    private Integer outfitId;

    private SeasonDto season;

    private Color color;

    private Occasion occasion;

    private LocalDate dateAdded;

    private Integer timesWorn;

    private Boolean unavailable;

    private String imageUrl;

    private List<ItemResponseDto> items;
}