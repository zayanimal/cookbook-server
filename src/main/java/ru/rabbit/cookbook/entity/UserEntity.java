package ru.rabbit.cookbook.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserEntity {

    @Id
    private String id;

    private String username;

    private String role;

    private String name;
}
