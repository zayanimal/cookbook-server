package ru.rabbit.cookbook.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    private String username;

    private String password;

    private String role;

    private String name;
}
