package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.example.ecloset.config.WeatherProperties;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.exception.WeatherException;
import org.example.ecloset.remote.WttrApi;
import org.example.ecloset.repository.UserRepository;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WttrApi wttrApi;
    private final UserRepository userRepository;
    private final WeatherProperties props;

    public String getCurrentWeatherForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));

        BigDecimal lat = user.getLatitude();
        BigDecimal lon = user.getLongitude();

        if (lat == null || lon == null) {
            throw new WeatherException("Координаты пользователя не установлены. Обновите профиль.");
        }

        String locationParam = lat + "," + lon;

        try {
            Response<ResponseBody> response = wttrApi
                    .getCurrentWeather(locationParam, props.getDefaultFormat())
                    .execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Сырой JSON
            }

            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Неизвестная ошибка";
            log.error("Ошибка при получении погоды от wttr.in. Код: {}, Тело: {}", response.code(), errorBody);
            throw new WeatherException("Не удалось получить данные о погоде (код: " + response.code() + ")");

        } catch (IOException e) {
            log.error("Сетевая ошибка при обращении к wttr.in", e);
            throw new WeatherException("Сетевая ошибка при получении погоды");
        }
    }
}