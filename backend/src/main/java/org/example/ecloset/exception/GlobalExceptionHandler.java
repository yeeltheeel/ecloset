package org.example.ecloset.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.example.ecloset.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- Пользователи / аутентификация ---

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("Неверный email или пароль", HttpStatus.UNAUTHORIZED);
    }

    // --- Доменные сущности ---

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleItemNotFound(ItemNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeasonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSeasonNotFound(SeasonNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutfitNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOutfitNotFound(OutfitNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // --- Файлы / изображения ---

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorage(FileStorageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidImageUrlException.class)
    public ResponseEntity<Map<String, String>> handleInvalidImageUrl(InvalidImageUrlException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return buildErrorResponse("Размер файла превышает допустимый", HttpStatus.BAD_REQUEST);
    }

    // --- AI-сервис ---

    @ExceptionHandler(NoClothingDetectedException.class)
    public ResponseEntity<Map<String, String>> handleNoClothingDetected(NoClothingDetectedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY); // 422
    }

    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<Map<String, String>> handleAiService(AiServiceException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

    @ExceptionHandler(AiProcessingException.class)
    public ResponseEntity<Map<String, String>> handleAiProcessing(AiProcessingException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_GATEWAY); // 502
    }

    // --- Погода ---

    @ExceptionHandler(CoordinatesNotSetException.class)
    public ResponseEntity<Map<String, String>> handleCoordinatesNotSet(CoordinatesNotSetException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<Map<String, String>> handleWeatherException(WeatherException ex) {
        log.warn("Ошибка погодного сервиса: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_GATEWAY); // 502
    }

    // --- Валидация / разбор запроса ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Ошибка валидации");
        body.put("details", fieldErrors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumParsingError(HttpMessageNotReadableException ex) {
        String message = "Некорректный формат данных в запросе";
        if (ex.getMessage() != null && ex.getMessage().contains("Cannot deserialize value of type")) {
            message = "Некорректное значение перечисления (enum). Проверьте правильность написания.";
        }
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
        return buildErrorResponse("Параметр '" + ex.getParameterName() + "' обязателен", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Некорректное значение параметра '" + ex.getName() + "'";
        Class<?> required = ex.getRequiredType();
        if (required != null && required.isEnum()) {
            message = "Некорректное значение перечисления (enum). Проверьте правильность написания.";
        }
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPart(MissingServletRequestPartException ex) {
        return buildErrorResponse("Обязательная часть запроса отсутствует: " + ex.getRequestPartName(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Необработанное исключение", ex);
        return buildErrorResponse("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", message);
        return new ResponseEntity<>(errorBody, status);
    }
}