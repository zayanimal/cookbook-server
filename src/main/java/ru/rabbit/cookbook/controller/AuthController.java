package ru.rabbit.cookbook.controller;

import static java.util.Objects.isNull;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabbit.cookbook.dto.ErrorResponse;
import ru.rabbit.cookbook.dto.LoginRequest;
import ru.rabbit.cookbook.dto.SuccessResponse;
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
    public ResponseEntity<?> login(final @Valid @RequestBody LoginRequest request) {
        try {
            val response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (final BadCredentialsException e) {
            return ResponseEntity.status(401)
                .body(ErrorResponse.builder()
                    .error("Неверное имя пользователя или пароль")
                    .success(false)
                    .build());
        }
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
        val authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isNull(authentication)) {
            throw new RuntimeException();
        }

        val user = authService.getCurrentUser(authentication.getName());

        return ResponseEntity.ok(Map.of(
        "user", user,
        "success", true));
    }
}
