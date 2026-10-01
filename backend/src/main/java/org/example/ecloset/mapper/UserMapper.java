package org.example.ecloset.mapper;

import org.example.ecloset.dto.request.RegisterRequest;
import org.example.ecloset.dto.response.AuthResponseDto;
import org.example.ecloset.dto.response.UserProfileDto;
import org.example.ecloset.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // RegisterRequest -> User (регистрация)
    public User toEntity(RegisterRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthday(request.getBirthday())
                .build();
    }

    // User -> UserProfileDto (get в профиле)
    public UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .location(user.getLocation())
                .build();
    }

    // User + токены -> AuthResponseDto (возвращает при авторизации)
    public AuthResponseDto toAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}