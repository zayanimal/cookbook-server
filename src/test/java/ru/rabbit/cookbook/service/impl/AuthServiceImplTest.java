package ru.rabbit.cookbook.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rabbit.cookbook.dto.LoginRequest;
import ru.rabbit.cookbook.dto.User;
import ru.rabbit.cookbook.entity.UserEntity;
import ru.rabbit.cookbook.mapper.UserMapper;
import ru.rabbit.cookbook.repository.AuthRepository;
import ru.rabbit.cookbook.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Получение пользователя — бросает исключение если пользователь не найден")
    void getUser_throwsWhenUserNotFound() {
        when(authRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> authService.getUser("unknown"));
    }

    @Test
    @DisplayName("Получение пользователя — возвращает маппированного пользователя")
    void getUser_returnsMappedUser() {
        val userEntity = new UserEntity();
        userEntity.setUsername("admin");

        val user = new User();
        user.setUsername("admin");

        when(authRepository.findByUsername("admin")).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(user);

        val result = authService.getUser("admin");

        assertEquals("admin", result.getUsername());
    }

    @Test
    @DisplayName("Авторизация — бросает исключение если пользователь не найден")
    void login_throwsWhenUserNotFound() {
        when(authRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(
            BadCredentialsException.class,
            () -> authService.login(new LoginRequest("unknown", "pass"))
        );
    }

    @Test
    @DisplayName("Авторизация — бросает исключение при неверном пароле")
    void login_throwsWhenPasswordDoesNotMatch() {
        val userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPassword("hashed");

        when(authRepository.findByUsername("admin")).thenReturn(userEntity);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(
            BadCredentialsException.class,
            () -> authService.login(new LoginRequest("admin", "wrong"))
        );
    }

    @Test
    @DisplayName("Авторизация — возвращает токен и данные пользователя при успехе")
    void login_returnsLoginResponseOnSuccess() {
        val userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPassword("hashed");
        userEntity.setRole("ROLE_ADMIN");

        val user = new User();
        user.setUsername("admin");

        when(authRepository.findByUsername("admin")).thenReturn(userEntity);
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);
        when(jwtTokenProvider.generateToken("admin", "ROLE_ADMIN")).thenReturn("jwt.token");
        when(userMapper.toDto(userEntity)).thenReturn(user);

        val result = authService.login(new LoginRequest("admin", "password"));

        assertTrue(result.getSuccess());
        assertEquals("jwt.token", result.getToken());
        assertEquals(user, result.getUser());
    }

    @Test
    @DisplayName("Получение текущего пользователя — делегирует вызов методу getUser")
    void getCurrentUser_delegatesToGetUser() {
        val userEntity = new UserEntity();
        userEntity.setUsername("admin");

        val user = new User();
        user.setUsername("admin");

        when(authRepository.findByUsername("admin")).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(user);

        val result = authService.getCurrentUser("admin");

        assertEquals(user, result);
    }
}
