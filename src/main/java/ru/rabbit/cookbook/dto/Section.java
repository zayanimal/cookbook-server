package ru.rabbit.cookbook.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<Page> pages = List.of();
}
