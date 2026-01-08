package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO для представления раздела документации
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    private String id;

    private String title;

    private List<Page> pages;
}
