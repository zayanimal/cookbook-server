package ru.rabbit.cookbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubsectionRequest {

    @NotBlank(message = "Название подраздела не может быть пустым")
    @Size(min = 1, message = "Название подраздела должно содержать хотя бы один символ")
    private String title;
}
