package ru.rabbit.cookbook.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса обновления страницы
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePageRequest {

    @Size(min = 1, message = "Название страницы должно содержать хотя бы один символ")
    private String title;

    private EditorJSContent content;
}
