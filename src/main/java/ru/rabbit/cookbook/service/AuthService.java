package ru.rabbit.cookbook.service;

import ru.rabbit.cookbook.dto.LoginRequest;
import ru.rabbit.cookbook.dto.LoginResponse;
import ru.rabbit.cookbook.dto.User;

public interface AuthService {

    User getUser(String username);

    LoginResponse login(LoginRequest request);

    User getCurrentUser(String username);
}
