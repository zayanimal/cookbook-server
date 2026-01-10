package ru.rabbit.cookbook.mapper;

import org.mapstruct.Mapper;
import ru.rabbit.cookbook.dto.User;
import ru.rabbit.cookbook.entity.UserEntity;

/**
 * Mapper для преобразования UserEntity в User DTO
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует UserEntity в User DTO
     *
     * @param userEntity сущность пользователя
     * @return DTO пользователя
     */
    User toDto(UserEntity userEntity);
}
