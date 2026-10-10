package org.example.ecloset.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с ошибкой валидации")
public class ValidationErrorResponse {

    @Schema(description = "Общее сообщение", example = "Ошибка валидации")
    private String error;

    @Schema(description = "Детали по каждому полю",
            example = "{\"email\": \"Некорректный email\", \"password\": \"Слишком короткий пароль\"}")
    private Map<String, String> details;
}