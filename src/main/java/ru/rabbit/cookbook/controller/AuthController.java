package ru.rabbit.cookbook.controller;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.rabbit.cookbook.service.AuthService;

/**
 * Контроллер для операций аутентификации и авторизации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизация пользователя
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(final @Valid @RequestBody LoginRequest request) {
        val user = authService.getUser(request.getUsername());
        val response = new LoginResponse();
        response.setSuccess(Boolean.TRUE);
        response.setUser(user);
        response.setToken("-");

        return ResponseEntity.ok(response);
    }

    /**
     * Выход пользователя
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout() {
        return ResponseEntity.ok(SuccessResponse.builder()
            .success(true)
            .message("Выход выполнен успешно")
            .build());
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
