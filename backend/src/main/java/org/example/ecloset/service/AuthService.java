package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.LoginRequest;
import org.example.ecloset.dto.request.RefreshTokenRequest;
import org.example.ecloset.dto.request.RegisterRequest;
import org.example.ecloset.dto.response.AuthResponseDto;
import org.example.ecloset.entity.RefreshToken;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.InvalidCredentialsException;
import org.example.ecloset.exception.RefreshTokenNotFoundException;
import org.example.ecloset.exception.UserAlreadyExistsException;
import org.example.ecloset.mapper.UserMapper;
import org.example.ecloset.repository.RefreshTokenRepository;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Transactional
    public AuthResponseDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Имя пользователя уже занято");
        }

        User user = userMapper.toEntity(request, passwordEncoder);
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = createRefreshToken(user);

        return userMapper.toAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponseDto login(LoginRequest request) {
        // AuthenticationManager сам проверит пароль через CustomUserDetailsService и PasswordEncoder
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Неверный email или пароль"));

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = createRefreshToken(user);

        return userMapper.toAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequest request) {
        String requestToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token не найден"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenNotFoundException("Refresh token истёк");
        }

        User user = refreshToken.getUser();

        // Удаляем старый токен и создаём новый (rotate tokens для безопасности)
        refreshTokenRepository.delete(refreshToken);
        String newRefreshToken = createRefreshToken(user);
        String newAccessToken = jwtService.generateAccessToken(user.getEmail());

        return userMapper.toAuthResponse(user, newAccessToken, newRefreshToken);
    }

    // Вспомогательный метод: создаёт refresh token и сохраняет в БД
    private String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }
}