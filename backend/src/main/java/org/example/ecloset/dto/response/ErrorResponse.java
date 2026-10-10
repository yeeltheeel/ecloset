package org.example.ecloset.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {
    @Schema(description = "Сообщение об ошибке", example = "Пользователь не найден")
    private String error;
}