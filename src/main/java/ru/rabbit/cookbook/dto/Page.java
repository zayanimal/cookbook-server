package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

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

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
