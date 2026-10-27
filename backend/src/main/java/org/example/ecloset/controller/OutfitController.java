package org.example.ecloset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.OutfitCreateRequest;
import org.example.ecloset.dto.request.OutfitUpdateRequest;
import org.example.ecloset.dto.response.ErrorResponse;
import org.example.ecloset.dto.response.OutfitResponseDto;
import org.example.ecloset.dto.response.ValidationErrorResponse;
import org.example.ecloset.entity.User;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.service.OutfitService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/outfits")
@RequiredArgsConstructor
@Tag(name = "Outfits", description = "Управление образами")
public class OutfitController {

    private final OutfitService outfitService;
    private final UserRepository userRepository;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));
        return user.getUserId();
    }

    @Operation(summary = "Получить образ по ID",
            description = "Возвращает один образ пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Образ найден",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ не найден или не принадлежит Вам.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Образ не найден или не принадлежит Вам.\"}")))
    })
    @GetMapping("/{outfitId}")
    public ResponseEntity<OutfitResponseDto> getOutfitById(@PathVariable Integer outfitId) {
        return ResponseEntity.ok(outfitService.getOutfitById(getCurrentUserId(), outfitId));
    }

    @Operation(summary = "Получить все образы текущего пользователя",
            description = "Возвращает список всех образов, принадлежащих авторизованному пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список образов успешно получен",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Пользователь не найден в БД\"}")))
    })
    @GetMapping
    public ResponseEntity<List<OutfitResponseDto>> getAllOutfits() {
        return ResponseEntity.ok(outfitService.getAllOutfitsByUser(getCurrentUserId()));
    }

    @Operation(summary = "Создать новый образ",
            description = "Создаёт новый образ для текущего пользователя. "
                    + "multipart/form-data: `data` — JSON с параметрами образа, `image` — файл изображения.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Образ успешно создан",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных или файла",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Указанный сезон или одна из вещей не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь с ID 5 не найдена\"}")))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OutfitResponseDto> createOutfit(
            @Valid @RequestPart("data") OutfitCreateRequest request,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201)
                .body(outfitService.createOutfit(getCurrentUserId(), request, image));
    }

    @Operation(summary = "Обновить образ",
            description = "Полностью обновляет данные образа по его ID"
                    + "multipart/form-data: `data` — JSON, `image` — необязательный новый файл.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Образ успешно обновлен",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или файла",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ не найден или не принадлежит Вам.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Образ не найден или не принадлежит Вам.\"}")))
    })
    @PutMapping(value = "/{outfitId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OutfitResponseDto> updateOutfit(
            @PathVariable Integer outfitId,
            @Valid @RequestPart("data") OutfitUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(outfitService.updateOutfit(getCurrentUserId(), outfitId, request, image));
    }

    @Operation(summary = "Удалить образ",
            description = "Удаляет образ из гардероба пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Образ успешно удален"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ не найден или не принадлежит Вам.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Образ не найден или не принадлежит Вам.\"}")))
    })
    @DeleteMapping("/{outfitId}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Integer outfitId) {
        outfitService.deleteOutfit(getCurrentUserId(), outfitId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Надеть образ",
            description = "Увеличивает счётчик носки на 1 для указанного образа и всех вещей в нём")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счётчик успешно увеличен",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ не найден или не принадлежит Вам",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Образ не найден или не принадлежит Вам\"}")))
    })
    @PostMapping("/{outfitId}/wear")
    public ResponseEntity<OutfitResponseDto> wearOutfit(@PathVariable Integer outfitId) {
        return ResponseEntity.ok(outfitService.wearOutfit(getCurrentUserId(), outfitId));
    }

    @Operation(summary = "Добавить вещь в образ",
            description = "Добавляет указанную вещь в существующий образ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вещь успешно добавлена в образ",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ или вещь не найдены, или не принадлежат Вам",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Вещь не найдена или не принадлежит Вам\"}")))
    })
    @PostMapping("/{outfitId}/items/{itemId}")
    public ResponseEntity<OutfitResponseDto> addItemToOutfit(
            @PathVariable Integer outfitId,
            @PathVariable Integer itemId) {
        return ResponseEntity.ok(outfitService.addItemToOutfit(getCurrentUserId(), outfitId, itemId));
    }

    @Operation(summary = "Заменить вещь в образе",
            description = "Удаляет старую вещь из образа и добавляет новую на её место.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вещь успешно заменена",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Образ или новая вещь не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Новая вещь не найдена\"}")))
    })
    @PutMapping("/{outfitId}/items/{oldItemId}/replace/{newItemId}")
    public ResponseEntity<OutfitResponseDto> replaceItemInOutfit(
            @PathVariable Integer outfitId,
            @PathVariable Integer oldItemId,
            @PathVariable Integer newItemId) {
        return ResponseEntity.ok(outfitService.replaceItemInOutfit(
                getCurrentUserId(), outfitId, oldItemId, newItemId));
    }

    @Operation(summary = "Найти образы по температуре",
            description = "Возвращает образы пользователя, сезоны которых подходят под указанную температуру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список подходящих образов",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Температура не указана",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Параметр temp обязателен\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/temperature")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByTemperature(@RequestParam Integer temp) {
        return ResponseEntity.ok(outfitService.getOutfitsByTemperature(getCurrentUserId(), temp));
    }

    @Operation(summary = "Найти образы по times worn",
            description = "Возвращает образы с указанным количеством носок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список образов",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Параметр count не указан",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Параметр count обязателен\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/times-worn")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByTimesWorn(@RequestParam Integer count) {
        return ResponseEntity.ok(outfitService.getOutfitsByTimesWorn(getCurrentUserId(), count));
    }

    @Operation(summary = "Найти образы по цвету",
            description = "Возвращает образы указанной цветовой гаммы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список образов",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение цвета",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/color")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByColor(@RequestParam Color color) {
        return ResponseEntity.ok(outfitService.getOutfitsByColor(getCurrentUserId(), color));
    }

    @Operation(summary = "Найти образы по поводу",
            description = "Возвращает образы для указанного повода")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список образов",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение повода",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/occasion")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByOccasion(@RequestParam Occasion occasion) {
        return ResponseEntity.ok(outfitService.getOutfitsByOccasion(getCurrentUserId(), occasion));
    }

    @Operation(summary = "Найти недоступные образы",
            description = "Возвращает образы, которые помечены как недоступные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список недоступных образов",
                    content = @Content(schema = @Schema(implementation = OutfitResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class,
                            example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/unavailable")
    public ResponseEntity<List<OutfitResponseDto>> getUnavailableOutfits() {
        return ResponseEntity.ok(outfitService.getUnavailableOutfits(getCurrentUserId()));
    }
}