package ru.rabbit.cookbook.service;

import ru.rabbit.cookbook.dto.User;

public interface AuthService {

    User getUser(String username);
}
