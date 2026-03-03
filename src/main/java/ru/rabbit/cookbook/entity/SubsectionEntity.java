package ru.rabbit.cookbook.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "subsections")
public class SubsectionEntity {

    @Id
    private String id;

    private String sectionId;

    private String title;
}
