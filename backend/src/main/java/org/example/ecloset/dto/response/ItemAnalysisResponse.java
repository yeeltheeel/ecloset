package org.example.ecloset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAnalysisResponse {

    // URL обработанного изображения (PNG с прозрачным фоном)
    private String imageUrl;

    private Category predictedCategory;

    private Double categoryConfidence;

    private List<Color> predictedColors;

    private List<String> rawColors;
}