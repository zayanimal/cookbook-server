package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
