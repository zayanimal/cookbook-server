package ru.rabbit.cookbook.controller;

import java.time.OffsetDateTime;
import java.util.List;
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
import ru.rabbit.cookbook.dto.CreatePageRequest;
import ru.rabbit.cookbook.dto.EditorJSContent;
import ru.rabbit.cookbook.dto.Page;
import ru.rabbit.cookbook.dto.PageUpdateParams;
import ru.rabbit.cookbook.dto.SuccessResponse;
import ru.rabbit.cookbook.dto.UpdatePageRequest;
import ru.rabbit.cookbook.service.PageService;

/**
 * Контроллер для управления страницами документации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sections/{sectionId}/pages")
public class PageController {

    private final PageService pageService;

    /**
     * Получить все страницы раздела
     * GET /api/sections/{sectionId}/pages
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPagesBySection(final @PathVariable String sectionId) {
        return ResponseEntity.ok(Map.of(
        "data", pageService.getPages(sectionId),
        "success", true));
    }

    /**
     * Создать новую страницу
     * POST /api/sections/{sectionId}/pages
     * Требует роль администратора
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPage(
        final @PathVariable String sectionId,
        final @Valid @RequestBody CreatePageRequest request
    ) {
        return ResponseEntity.ok(Map.of(
        "data", pageService.createPage(sectionId, request),
        "success", true));
    }

    /**
     * Получить страницу по ID
     * GET /api/sections/{sectionId}/pages/{pageId}
     */
    @GetMapping("/{pageId}")
    public ResponseEntity<Map<String, Object>> getPageById(
        final @PathVariable String sectionId,
        final @PathVariable String pageId
    ) {
        // TODO: Реализовать получение страницы по ID
        val page = Page.builder()
            .id(pageId)
            .title("Пример страницы")
            .content(EditorJSContent.builder()
                .blocks(List.of())
                .build())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

        return ResponseEntity.ok(Map.of(
        "data", page,
        "success", true));
    }

    /**
     * Обновить страницу
     * PUT /api/sections/{sectionId}/pages/{pageId}
     * Требует роль администратора
     */
    @PutMapping("/{pageId}")
    public ResponseEntity<Map<String, Object>> updatePage(
        final @PathVariable String sectionId,
        final @PathVariable String pageId,
        final @Valid @RequestBody UpdatePageRequest request
    ) {
        return ResponseEntity.ok(Map.of(
        "data", pageService.updatePage(PageUpdateParams.builder()
            .pageId(pageId)
            .sectionId(sectionId)
            .request(request)
            .build()),
        "success", true));
    }

    /**
     * Удалить страницу
     * DELETE /api/sections/{sectionId}/pages/{pageId}
     * Требует роль администратора
     */
    @DeleteMapping("/{pageId}")
    public ResponseEntity<SuccessResponse> deletePage(
        final @PathVariable String sectionId,
        final @PathVariable String pageId
    ) {
        pageService.deletePage(sectionId, pageId);

        return ResponseEntity.ok(SuccessResponse.builder()
            .success(true)
            .message("Страница успешно удалена")
            .build());
    }
}
