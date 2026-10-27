package org.example.ecloset.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {

    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String username;

    private LocalDate birthday;

    @DecimalMin(value = "-90.0", message = "Некорректная широта")
    @DecimalMax(value = "90.0", message = "Некорректная широта")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Некорректная долгота")
    @DecimalMax(value = "180.0", message = "Некорректная долгота")
    private BigDecimal longitude;
}