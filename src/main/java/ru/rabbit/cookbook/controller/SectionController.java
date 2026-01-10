package ru.rabbit.cookbook.controller;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.rabbit.cookbook.service.SectionService;

/**
 * Контроллер для управления разделами документации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    /**
     * Получить все разделы
     * GET /api/sections
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSections() {
        return ResponseEntity.ok(Map.of(
            "data", sectionService.getSections(),
            "success", true));
    }

    /**
     * Создать новый раздел
     * POST /api/sections
     * Требует роль администратора
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSection(final @Valid @RequestBody CreateSectionRequest request) {
        return ResponseEntity.ok(Map.of(
            "data", sectionService.createSection(request),
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
        return ResponseEntity.ok(Map.of(
        "data", sectionService.updateSection(sectionId, request),
        "success", true));
    }

    /**
     * Удалить раздел
     * DELETE /api/sections/{sectionId}
     * Требует роль администратора
     */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<SuccessResponse> deleteSection(final @PathVariable String sectionId) {
        sectionService.deleteSection(sectionId);
        return ResponseEntity.ok(SuccessResponse.builder()
            .success(true)
            .message(String.format("Раздел успешно удален id: %s", sectionId))
            .build());
    }
}
