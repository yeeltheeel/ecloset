package org.example.ecloset.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    @NotNull(message = "Категория обязательна")
    private Category category;

    // null означает "без сезона"
    private Integer seasonId;

    private Color color;

    private Material material;

    private Occasion occasion;

    @NotBlank(message = "URL изображения обязателен")
    @Size(max = 500, message = "URL изображения не может быть длиннее 500 символов")
    private String imageUrl;

    @Size(max = 100, message = "Название бренда не может быть длиннее 100 символов")
    private String brand;

    @DecimalMin(value = "0.0", inclusive = true, message = "Цена не может быть отрицательной")
    private BigDecimal price;
}