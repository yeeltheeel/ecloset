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
import org.example.ecloset.dto.response.ItemAnalysisResponse;
import org.example.ecloset.dto.response.ItemResponseDto;
import org.example.ecloset.dto.response.ValidationErrorResponse;
import org.example.ecloset.entity.User;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.service.ItemService;
import org.example.ecloset.service.WeatherService;
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
    private final WeatherService weatherService;
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
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена или не принадлежит Вам.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Вещь не найдена или не принадлежит Вам.\"}")))
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Integer itemId) {
        return ResponseEntity.ok(itemService.getItemById(getCurrentUserId(), itemId));
    }

    @Operation(summary = "Получить все вещи текущего пользователя",
            description = "Возвращает список всех вещей, принадлежащих авторизованному пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей успешно получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Текущий пользователь не найден в БД\"}")))
    })
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItemsByUser(getCurrentUserId()));
    }

    @Operation(summary = "Создать новую вещь",
            description = "Добавляет новую вещь в гардероб текущего пользователя. Тело — application/json. "
                    + "Фото заранее загружается через POST /api/items/analyze; "
                    + "сюда передаётся imageUrl из ответа анализа.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вещь успешно создана",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации полей или imageUrl",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Указанный сезон не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Сезон с ID 5 не найден\"}")))
    })
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemCreateRequest request) {
        return ResponseEntity.status(201)
                .body(itemService.createItem(getCurrentUserId(), request));
    }

    @Operation(summary = "Обновить вещь",
            description = "Полностью обновляет данные вещи по её ID (PUT-логика), тело — application/json. "
                    + "Если передан новый imageUrl (из /api/items/analyze) — фото заменяется, старый файл удаляется; "
                    + "если imageUrl не передан — остаётся текущее фото.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вещь успешно обновлена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации полей или imageUrl",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена или не принадлежит Вам.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Вещь не найдена или не принадлежит Вам.\"}")))
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Integer itemId,
            @Valid @RequestBody ItemUpdateRequest request) {
        return ResponseEntity.ok(itemService.updateItem(getCurrentUserId(), itemId, request));
    }

    @Operation(summary = "Удалить вещь",
            description = "Безвозвратно удаляет вещь из гардероба пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Вещь успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена или не принадлежит Вам.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Вещь не найдена или не принадлежит Вам.\"}")))
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
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена или не принадлежит Вам",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Вещь не найдена или не принадлежит Вам\"}")))
    })
    @PostMapping("/{itemId}/wear")
    public ResponseEntity<ItemResponseDto> wearItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(itemService.wearItem(getCurrentUserId(), itemId));
    }

    @Operation(summary = "Найти вещи по температуре",
            description = "Возвращает вещи пользователя, сезоны которых подходят под указанную температуру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список подходящих вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Температура не указана",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Параметр 'temp' обязателен\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/temperature")
    public ResponseEntity<List<ItemResponseDto>> getItemsByTemperature(@RequestParam Integer temp) {
        return ResponseEntity.ok(itemService.getItemsByTemperature(getCurrentUserId(), temp));
    }

    @Operation(summary = "Получить вещи по текущей погоде",
            description = "Определяет текущую температуру по координатам пользователя (wttr.in) "
                    + "и возвращает вещи, сезоны которых подходят под эту температуру. "
                    + "Если сервис вернул диапазон — берётся среднее.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список подходящих вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Координаты пользователя не установлены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Координаты пользователя не установлены. Обновите профиль.\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Текущий пользователь не найден в БД\"}"))),
            @ApiResponse(responseCode = "502", description = "Погодный сервис недоступен или вернул некорректный ответ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Сетевая ошибка при получении температуры\"}")))
    })
    @GetMapping("/weather")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCurrentWeather() {
        Integer userId = getCurrentUserId();
        Integer temperature = weatherService.getCurrentTemperatureForUser(userId);
        return ResponseEntity.ok(itemService.getItemsByTemperature(userId, temperature));
    }

    @Operation(summary = "Найти вещи по times worn",
            description = "Возвращает вещи с указанным times worn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Параметр count не указан",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Параметр 'count' обязателен\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/times-worn")
    public ResponseEntity<List<ItemResponseDto>> getItemsByTimesWorn(@RequestParam Integer count) {
        return ResponseEntity.ok(itemService.getItemsByTimesWorn(getCurrentUserId(), count));
    }

    @Operation(summary = "Найти вещи по категории",
            description = "Возвращает вещи указанной категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение категории",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/category")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCategory(@RequestParam Category category) {
        return ResponseEntity.ok(itemService.getItemsByCategory(getCurrentUserId(), category));
    }

    @Operation(summary = "Найти вещи по цвету",
            description = "Возвращает вещи указанного цвета")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение цвета",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/color")
    public ResponseEntity<List<ItemResponseDto>> getItemsByColor(@RequestParam Color color) {
        return ResponseEntity.ok(itemService.getItemsByColor(getCurrentUserId(), color));
    }

    @Operation(summary = "Найти вещи по материалу",
            description = "Возвращает вещи из указанного материала")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение материала",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/material")
    public ResponseEntity<List<ItemResponseDto>> getItemsByMaterial(@RequestParam Material material) {
        return ResponseEntity.ok(itemService.getItemsByMaterial(getCurrentUserId(), material));
    }

    @Operation(summary = "Найти вещи по случаю",
            description = "Возвращает вещи для указанного повода")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вещей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректное значение повода",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Некорректное значение перечисления (enum)\"}"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}")))
    })
    @GetMapping("/occasion")
    public ResponseEntity<List<ItemResponseDto>> getItemsByOccasion(@RequestParam Occasion occasion) {
        return ResponseEntity.ok(itemService.getItemsByOccasion(getCurrentUserId(), occasion));
    }

    @Operation(summary = "Проанализировать фото вещи",
            description = "Загружает фото, сегментирует вещь (YOLOE-26), определяет категорию (Fashion-CLIP) "
                    + "и основные цвета (K-Means). Сохраняет только обработанное изображение (PNG без фона) "
                    + "и возвращает его URL и подсказки. Первый этап создания вещи: "
                    + "полученный imageUrl затем передаётся в POST /api/items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Анализ выполнен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ItemAnalysisResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации файла",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"Не авторизован\"}"))),
            @ApiResponse(responseCode = "422", description = "На фото не найдена вещь",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"На фото не найдена вещь. Попробуйте другое фото.\"}"))),
            @ApiResponse(responseCode = "502", description = "AI-сервис вернул некорректный результат",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"AI-сервис не вернул обработанное изображение\"}"))),
            @ApiResponse(responseCode = "503", description = "AI-сервис недоступен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"error\": \"AI-сервис недоступен\"}")))
    })
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemAnalysisResponse> analyzeItem(
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(itemService.analyzeImage(image));
    }
}