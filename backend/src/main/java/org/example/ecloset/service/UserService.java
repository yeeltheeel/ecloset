package org.example.ecloset.service;

import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ChangePasswordRequest;
import org.example.ecloset.dto.request.UpdateUserProfileRequest;
import org.example.ecloset.dto.response.UserProfileDto;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.InvalidCredentialsException;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.exception.UserAlreadyExistsException;
import org.example.ecloset.mapper.UserMapper;
import org.example.ecloset.repository.RefreshTokenRepository;
import org.example.ecloset.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }

        userRepository.save(user);
        return userMapper.toProfileDto(user);
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