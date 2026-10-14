package org.example.ecloset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Integer itemId;
    private Category category;
    private SeasonDto season;
    private Color color;
    private Material material;
    private Occasion occasion;
    private LocalDate dateAdded;
    private Integer timesWorn;
    private String imageUrl;
    private String brand;
    private BigDecimal price;
}