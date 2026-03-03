package ru.rabbit.cookbook.controller;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.rabbit.cookbook.dto.PageUpdateParams;
import ru.rabbit.cookbook.dto.SuccessResponse;
import ru.rabbit.cookbook.dto.UpdatePageRequest;
import ru.rabbit.cookbook.service.PageService;

/**
 * Контроллер для управления страницами документации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PageController {

    private final PageService pageService;

    @GetMapping("/subsections/{subsectionId}/pages")
    public ResponseEntity<Map<String, Object>> getPagesBySubsection(final @PathVariable String subsectionId) {
        return ResponseEntity.ok(Map.of(
            "data", pageService.getPages(subsectionId),
            "success", true));
    }

    @PostMapping("/subsections/{subsectionId}/pages")
    public ResponseEntity<Map<String, Object>> createPage(
        final @PathVariable String subsectionId,
        final @Valid @RequestBody CreatePageRequest request
    ) {
        return ResponseEntity.ok(Map.of(
            "data", pageService.createPage(subsectionId, request),
            "success", true));
    }

    @PutMapping("/pages/{pageId}")
    public ResponseEntity<Map<String, Object>> updatePage(
        final @PathVariable String pageId,
        final @Valid @RequestBody UpdatePageRequest request
    ) {
        return ResponseEntity.ok(Map.of(
            "data", pageService.updatePage(PageUpdateParams.builder()
                .pageId(pageId)
                .request(request)
                .build()),
            "success", true));
    }

    @DeleteMapping("/pages/{pageId}")
    public ResponseEntity<SuccessResponse> deletePage(final @PathVariable String pageId) {
        pageService.deletePage(pageId);
        return ResponseEntity.ok(SuccessResponse.builder()
            .success(true)
            .message("Страница успешно удалена")
            .build());
    }
}
