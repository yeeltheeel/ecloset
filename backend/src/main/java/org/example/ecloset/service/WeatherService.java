package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.example.ecloset.config.WeatherProperties;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.CoordinatesNotSetException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.exception.WeatherException;
import org.example.ecloset.remote.WttrApi;
import org.example.ecloset.repository.UserRepository;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WttrApi wttrApi;
    private final UserRepository userRepository;
    private final WeatherProperties props;

    private static final Pattern TEMPERATURE_RANGE =
            Pattern.compile("([+-]?\\d+)\\s*(?:\\.\\.|…|–|—|-)\\s*([+-]?\\d+)");
    private static final Pattern TEMPERATURE_SINGLE =
            Pattern.compile("[+-]?\\d+");

    public String getCurrentWeatherForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));

        BigDecimal lat = user.getLatitude();
        BigDecimal lon = user.getLongitude();

        if (lat == null || lon == null) {
            throw new CoordinatesNotSetException("Координаты пользователя не установлены. Обновите профиль.");
        }

        String locationParam = lat + "," + lon;

        try {
            Response<ResponseBody> response = wttrApi
                    .getCurrentWeather(locationParam, props.getDefaultFormat())
                    .execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }

            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Неизвестная ошибка";
            log.error("Ошибка при получении погоды от wttr.in. Код: {}, Тело: {}", response.code(), errorBody);
            throw new WeatherException("Не удалось получить данные о погоде (код: " + response.code() + ")");

        } catch (IOException e) {
            log.error("Сетевая ошибка при обращении к wttr.in", e);
            throw new WeatherException("Сетевая ошибка при получении погоды");
        }
    }

    public Integer getCurrentTemperatureForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));

        BigDecimal lat = user.getLatitude();
        BigDecimal lon = user.getLongitude();

        if (lat == null || lon == null) {
            throw new CoordinatesNotSetException("Координаты пользователя не установлены. Обновите профиль.");
        }

        String locationParam = lat + "," + lon;

        try {
            Response<ResponseBody> response = wttrApi.getCurrentWeather(locationParam, "%t").execute();

            if (response.isSuccessful() && response.body() != null) {
                return parseTemperature(response.body().string());
            }

            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Неизвестная ошибка";
            log.error("Ошибка при получении температуры от wttr.in. Код: {}, Тело: {}", response.code(), errorBody);
            throw new WeatherException("Не удалось получить данные о температуре (код: " + response.code() + ")");

        } catch (IOException e) {
            log.error("Сетевая ошибка при обращении к wttr.in", e);
            throw new WeatherException("Сетевая ошибка при получении температуры");
        }
    }

    private Integer parseTemperature(String raw) {
        String normalized = raw.replace('−', '-').trim();

        // Сначала диапазон — иначе "-10--5" распарсилось бы как два отдельных числа.
        Matcher rangeMatcher = TEMPERATURE_RANGE.matcher(normalized);
        if (rangeMatcher.find()) {
            int low = Integer.parseInt(rangeMatcher.group(1));
            int high = Integer.parseInt(rangeMatcher.group(2));
            return (int) Math.round((low + high) / 2.0);
        }

        Matcher singleMatcher = TEMPERATURE_SINGLE.matcher(normalized);
        if (singleMatcher.find()) {
            return Integer.parseInt(singleMatcher.group());
        }

        log.warn("Не удалось распарсить температуру из ответа wttr.in: '{}'", raw);
        throw new WeatherException("Не удалось определить температуру по ответу погодного сервиса");
    }
}