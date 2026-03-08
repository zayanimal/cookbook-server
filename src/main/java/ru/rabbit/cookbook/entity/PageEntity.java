package ru.rabbit.cookbook.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pages")
public class PageEntity {

    @Id
    private String id;

    private String subsectionId;

    private String title;

    private String content;

    private String createdAt;

    private String updatedAt;
}
