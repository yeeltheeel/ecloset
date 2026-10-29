package org.example.ecloset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.response.ErrorResponse;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.service.WeatherService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "Получение данных о погоде для текущего пользователя")
public class WeatherController {

    private final WeatherService weatherService;
    private final UserRepository userRepository;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"))
                .getUserId();
    }

    @Operation(summary = "Получить текущую погоду",
            description = "Возвращает сырой JSON с данными о погоде по координатам текущего авторизованного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о погоде успешно получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Требуется авторизация\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден или координаты не установлены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Координаты пользователя не установлены. Обновите профиль.\"}")))
    })
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrentWeather() {
        String rawJson = weatherService.getCurrentWeatherForUser(getCurrentUserId());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(rawJson);
    }
}