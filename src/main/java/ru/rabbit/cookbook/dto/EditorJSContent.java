package ru.rabbit.cookbook.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для содержимого EditorJS
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorJSContent {

    private List<EditorJSBlock> blocks;
}
