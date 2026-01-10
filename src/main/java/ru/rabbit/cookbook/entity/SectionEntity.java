package ru.rabbit.cookbook.entity;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SectionEntity {

    @Id
    private String id;

    private String title;

    private List<PageEntity> pages;
}
