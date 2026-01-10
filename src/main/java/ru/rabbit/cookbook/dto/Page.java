package ru.rabbit.cookbook.dto;

import java.time.OffsetDateTime;

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

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
