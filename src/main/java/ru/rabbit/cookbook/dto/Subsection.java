package ru.rabbit.cookbook.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subsection {

    private String id;

    private String title;

    private List<Page> pages = List.of();
}
