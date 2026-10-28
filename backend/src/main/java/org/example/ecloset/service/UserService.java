package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ChangePasswordRequest;
import org.example.ecloset.dto.request.UpdateUserProfileRequest;
import org.example.ecloset.dto.response.UserProfileDto;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.InvalidCredentialsException;
import org.example.ecloset.exception.UserAlreadyExistsException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.mapper.UserMapper;
import org.example.ecloset.repository.RefreshTokenRepository;
import org.example.ecloset.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return userMapper.toProfileDto(user);
    }

    @Transactional
    public UserProfileDto updateUserProfile(Integer userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (request.getUsername() != null) {
            if (!request.getUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(request.getUsername())) {
                throw new UserAlreadyExistsException("Имя пользователя уже занято");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }

        // Локация теперь = пара координат, обновляем строго вместе
        updateLocation(user, request.getLatitude(), request.getLongitude());

        // save() не обязателен: user — managed-объект в рамках @Transactional,
        // Hibernate сам сделает dirty checking и UPDATE при коммите.
        // Но оставил для наглядности/совместимости с вашим стилем.
        userRepository.save(user);

        return userMapper.toProfileDto(user);
    }

    private void updateLocation(User user, BigDecimal latitude, BigDecimal longitude) {
        boolean hasLatitude = latitude != null;
        boolean hasLongitude = longitude != null;

        // Ни то ни другое не прислали — координаты не трогаем (частичное обновление)
        if (!hasLatitude && !hasLongitude) {
            return;
        }

        // Прислали только одну координату — это ошибка, а не валидное местоположение
        if (!hasLatitude || !hasLongitude) {
            throw new IllegalArgumentException(
                    "Широта и долгота должны быть указаны вместе"
            );
        }

        if (latitude.compareTo(new BigDecimal("-90")) < 0
                || latitude.compareTo(new BigDecimal("90")) > 0
                || longitude.compareTo(new BigDecimal("-180")) < 0
                || longitude.compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Некорректные географические координаты");
        }

        user.setLatitude(latitude);
        user.setLongitude(longitude);
    }

    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Старый пароль неверен");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserProfile(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }
}