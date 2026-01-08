package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO для блока EditorJS
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorJSBlock {

    private String type; // header, paragraph, list, code, quote, table, image

    private Map<String, Object> data;
}
