package ru.rabbit.cookbook.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabbit.cookbook.dto.CreateSectionRequest;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.dto.SuccessResponse;
import ru.rabbit.cookbook.dto.UpdateSectionRequest;

/**
 * Контроллер для управления разделами документации
 */
@RestController
@RequestMapping("/api/sections")
public class SectionController {

    /**
     * Получить все разделы
     * GET /api/sections
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSections() {
        // TODO: Реализовать получение всех разделов из базы данных
        return ResponseEntity.ok(Map.of(
            "data", List.of(),
            "success", true));
    }

    /**
     * Создать новый раздел
     * POST /api/sections
     * Требует роль администратора
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSection(final @Valid @RequestBody CreateSectionRequest request) {
        // TODO: Реализовать создание раздела
        // TODO: Проверить роль пользователя (должен быть admin)
        val section = Section.builder()
            .id("1")
            .title(request.getTitle())
            .build();

        return ResponseEntity.ok(Map.of(
            "data", section,
            "success", true));
    }

    /**
     * Получить раздел по ID
     * GET /api/sections/{sectionId}
     */
    @GetMapping("/{sectionId}")
    public ResponseEntity<Map<String, Object>> getSectionById(final @PathVariable String sectionId) {
        // TODO: Реализовать получение раздела по ID
        val section = Section.builder()
            .id(sectionId)
            .title("Пример раздела")
            .build();

        return ResponseEntity.ok(Map.of(
            "data", section,
            "success", true));
    }

    /**
     * Обновить раздел
     * PUT /api/sections/{sectionId}
     * Требует роль администратора
     */
    @PutMapping("/{sectionId}")
    public ResponseEntity<Map<String, Object>> updateSection(
        final @PathVariable String sectionId,
        final @Valid @RequestBody UpdateSectionRequest request
    ) {
        val section = Section.builder()
            .id(sectionId)
            .title(request.getTitle())
            .build();

        return ResponseEntity.ok(Map.of(
            "data", section,
            "success", true));
    }

    /**
     * Удалить раздел
     * DELETE /api/sections/{sectionId}
     * Требует роль администратора
     */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<SuccessResponse> deleteSection(final @PathVariable String sectionId) {
        // TODO: Реализовать удаление раздела и всех его страниц
        // TODO: Проверить роль пользователя (должен быть admin)
        val response = SuccessResponse.builder()
            .success(true)
            .message("Раздел успешно удален")
            .build();

        return ResponseEntity.ok(response);
    }
}
