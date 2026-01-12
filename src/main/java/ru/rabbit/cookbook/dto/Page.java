package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления страницы документации
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private String id;

    private String title;

    private EditorJSContent content;
}
