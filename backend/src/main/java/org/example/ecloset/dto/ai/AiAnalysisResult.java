package org.example.ecloset.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResult {
    private String processedImageBase64;
    private String category;
    private Double categoryConfidence;
    private List<String> colorsHex;
}