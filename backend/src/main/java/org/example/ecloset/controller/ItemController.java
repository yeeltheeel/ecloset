package org.example.ecloset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ItemCreateRequest;
import org.example.ecloset.dto.request.ItemUpdateRequest;
import org.example.ecloset.dto.response.ErrorResponse;
import org.example.ecloset.dto.response.ItemResponseDto;
import org.example.ecloset.dto.response.ValidationErrorResponse;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Управление вещами гардероба пользователя")
public class ItemController {

    private final ItemService itemService;
    private final UserRepository userRepository;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));
        return user.getUserId();
    }

    @Operation(summary = "Получить вещь по ID",
            description = "Возвращает одну вещь текущего пользователя по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вещь найдена",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "403", description = "Вещь принадлежит другому пользователю",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"У вас нет прав на это действие\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь не найдена\"}")))
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Integer itemId) {
        return ResponseEntity.ok(itemService.getItemById(getCurrentUserId(), itemId));
    }

    @Operation(summary = "Получить все вещи текущего пользователя",
            description = "Возвращает список всех вещей, принадлежащих авторизованному пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей успешно получен",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Текущий пользователь не найден в БД\"}")))
    })
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItemsByUser(getCurrentUserId()));
    }

    @Operation(summary = "Создать новую вещь",
            description = "Добавляет новую вещь в гардероб текущего пользователя. "
                    + "Запрос multipart/form-data: часть `data` — JSON, часть `image` — файл изображения.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вещь успешно создана",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных или файла",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Указанный сезон не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Сезон с ID 5 не найден\"}")))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponseDto> createItem(
            @Valid @RequestPart("data") ItemCreateRequest request,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201)
                .body(itemService.createItem(getCurrentUserId(), request, image));
    }

    @Operation(summary = "Обновить вещь",
            description = "Полностью обновляет данные вещи по её ID (PUT-логика). "
                    + "Запрос multipart/form-data: часть `data` — JSON, часть `image` — необязательный новый файл.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вещь успешно обновлена",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или файла",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав на это действие (вещь принадлежит другому пользователю)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"У вас нет прав на это действие\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь или сезон не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь не найдена\"}")))
    })
    @PutMapping(value = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Integer itemId,
            @Valid @RequestPart("data") ItemUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(itemService.updateItem(getCurrentUserId(), itemId, request, image));
    }

    @Operation(summary = "Удалить вещь",
            description = "Безвозвратно удаляет вещь из гардероба пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Вещь успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав на это действие (вещь принадлежит другому пользователю)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"У вас нет прав на это действие\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь не найдена\"}")))
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer itemId) {
        itemService.deleteItem(getCurrentUserId(), itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Надеть вещь",
            description = "Увеличивает счётчик носки (times_worn) на 1 для указанной вещи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счётчик успешно увеличен",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "403", description = "У пользователя нет прав на это действие (вещь принадлежит другому пользователю)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"У вас нет прав на это действие\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь не найдена\"}")))
    })
    @PostMapping("/{itemId}/wear")
    public ResponseEntity<ItemResponseDto> wearItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(itemService.wearItem(getCurrentUserId(), itemId));
    }

    @Operation(summary = "Найти вещи по температуре",
            description = "Возвращает вещи пользователя, сезоны которых подходят под указанную температуру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список подходящих вещей",
                    content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Температура не указана",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Параметр temp обязателен\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/temperature")
    public ResponseEntity<List<ItemResponseDto>> getItemsByTemperature(@RequestParam Integer temp) {
        return ResponseEntity.ok(itemService.getItemsByTemperature(getCurrentUserId(), temp));
    }
}