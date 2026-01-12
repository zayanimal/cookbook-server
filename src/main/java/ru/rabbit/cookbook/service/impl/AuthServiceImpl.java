package ru.rabbit.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.LoginRequest;
import ru.rabbit.cookbook.dto.LoginResponse;
import ru.rabbit.cookbook.dto.User;
import ru.rabbit.cookbook.mapper.UserMapper;
import ru.rabbit.cookbook.repository.AuthRepository;
import ru.rabbit.cookbook.security.JwtTokenProvider;
import ru.rabbit.cookbook.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User getUser(final String username) {
        val userEntity = authRepository.findByUsername(username);

        if (userEntity == null) {
            throw new BadCredentialsException("Пользователь не найден");
        }

        return userMapper.toDto(userEntity);
    }

    @Override
    public LoginResponse login(final LoginRequest request) {
        val userEntity = authRepository.findByUsername(request.getUsername());

        if (userEntity == null) {
            throw new BadCredentialsException("Неверное имя пользователя или пароль");
        }

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("Неверное имя пользователя или пароль");
        }

        val token = jwtTokenProvider.generateToken(userEntity.getUsername(), userEntity.getRole());
        val user = userMapper.toDto(userEntity);

        return LoginResponse.builder()
            .token(token)
            .user(user)
            .success(true)
            .build();
    }

    @Override
    public User getCurrentUser(final String username) {
        return getUser(username);
    }
}
