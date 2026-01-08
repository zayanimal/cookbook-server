package ru.rabbit.cookbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса обновления раздела
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSectionRequest {

    @NotBlank(message = "Название раздела не может быть пустым")
    @Size(min = 1, message = "Название раздела должно содержать хотя бы один символ")
    private String title;
}
