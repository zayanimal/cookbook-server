package ru.rabbit.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для успешного ответа
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

    private Boolean success;

    private String message;
}
