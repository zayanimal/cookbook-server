package ru.rabbit.cookbook.entity;

import java.time.OffsetDateTime;

import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.rabbit.cookbook.dto.EditorJSContent;

@Data
public class PageEntity {

    @Id
    private String id;

    private String title;

    private EditorJSContent content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
