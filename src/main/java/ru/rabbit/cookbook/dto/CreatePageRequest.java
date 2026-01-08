package ru.rabbit.cookbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса создания страницы
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePageRequest {

    @NotBlank(message = "Название страницы не может быть пустым")
    @Size(min = 1, message = "Название страницы должно содержать хотя бы один символ")
    private String title;

    private EditorJSContent content;
}
