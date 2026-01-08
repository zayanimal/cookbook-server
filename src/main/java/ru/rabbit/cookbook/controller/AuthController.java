package ru.rabbit.cookbook.controller;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabbit.cookbook.dto.LoginRequest;
import ru.rabbit.cookbook.dto.LoginResponse;
import ru.rabbit.cookbook.dto.SuccessResponse;
import ru.rabbit.cookbook.dto.User;

/**
 * Контроллер для операций аутентификации и авторизации
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Авторизация пользователя
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // TODO: Реализовать логику аутентификации
        // Временная заглушка для примера
        val response = LoginResponse.builder()
            .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            .user(User.builder()
                .id("1")
                .username(request.getUsername())
                .role("admin")
                .name("Администратор")
                .build())
            .success(true)
            .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Выход пользователя
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout() {
        // TODO: Реализовать логику инвалидации токена
        val response = SuccessResponse.builder()
            .success(true)
            .message("Выход выполнен успешно")
            .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Получить данные текущего пользователя
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        // TODO: Реализовать получение текущего пользователя из токена
        return ResponseEntity.ok(Map.of(
            "user", User.builder()
                .id("1")
                .username("admin")
                .role("admin")
                .name("Администратор")
                .build(),
            "success", true));
    }
}
