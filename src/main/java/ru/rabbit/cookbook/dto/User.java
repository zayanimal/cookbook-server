package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String username;

    private String role;

    private String name;
}
