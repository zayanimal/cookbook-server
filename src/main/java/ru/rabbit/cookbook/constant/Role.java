package ru.rabbit.cookbook.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("Администратор"),

    CLIENT("Клиент");

    private final String role;
}
