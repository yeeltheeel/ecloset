package org.example.ecloset.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutfitCreateRequest {

    private Integer seasonId;

    private Color color;

    private Occasion occasion;

    @NotEmpty(message = "Аутфит должен содержать хотя бы одну вещь")
    @Size(max = 20, message = "Аутфит не может содержать более 20 вещей")
    private List<Integer> itemIds;
}