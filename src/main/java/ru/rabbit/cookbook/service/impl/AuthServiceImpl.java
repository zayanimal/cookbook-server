package ru.rabbit.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.User;
import ru.rabbit.cookbook.mapper.UserMapper;
import ru.rabbit.cookbook.repository.AuthRepository;
import ru.rabbit.cookbook.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final AuthRepository authRepository;

    @Override
    public User getUser(final String username) {
        return userMapper.toDto(authRepository.findByUsername(username));
    }
}
