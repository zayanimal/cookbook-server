package ru.rabbit.cookbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rabbit.cookbook.entity.UserEntity;


public interface AuthRepository extends MongoRepository<UserEntity, String> {

    UserEntity findByUsername(String login);
}
