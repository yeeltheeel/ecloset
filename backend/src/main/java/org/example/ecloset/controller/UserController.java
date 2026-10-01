package org.example.ecloset.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecloset.dto.request.ChangePasswordRequest;
import org.example.ecloset.dto.request.UpdateUserProfileRequest;
import org.example.ecloset.dto.response.UserProfileDto;
import org.example.ecloset.entity.User;
import org.example.ecloset.exception.UserNotFoundException;
import org.example.ecloset.repository.UserRepository;
import org.example.ecloset.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));
        return user.getUserId();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        Integer userId = getCurrentUserId();
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        Integer userId = getCurrentUserId();
        return ResponseEntity.ok(userService.updateUserProfile(userId, request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Integer userId = getCurrentUserId();
        userService.changePassword(userId, request);
        return ResponseEntity.ok().build(); // 200 OK без тела
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile() {
        Integer userId = getCurrentUserId();
        userService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}