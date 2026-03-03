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
import ru.rabbit.cookbook.dto.CreateSubsectionRequest;
import ru.rabbit.cookbook.dto.SuccessResponse;
import ru.rabbit.cookbook.dto.UpdateSubsectionRequest;
import ru.rabbit.cookbook.service.SubsectionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubsectionController {

    private final SubsectionService subsectionService;

    @GetMapping("/sections/{sectionId}/subsections")
    public ResponseEntity<Map<String, Object>> getSubsectionsBySection(final @PathVariable String sectionId) {
        return ResponseEntity.ok(Map.of(
            "data", subsectionService.getSubsections(sectionId),
            "success", true));
    }

    @PostMapping("/sections/{sectionId}/subsections")
    public ResponseEntity<Map<String, Object>> createSubsection(
        final @PathVariable String sectionId,
        final @Valid @RequestBody CreateSubsectionRequest request
    ) {
        return ResponseEntity.ok(Map.of(
            "data", subsectionService.createSubsection(sectionId, request),
            "success", true));
    }

    @GetMapping("/subsections/{subsectionId}")
    public ResponseEntity<Map<String, Object>> getSubsectionById(final @PathVariable String subsectionId) {
        return ResponseEntity.ok(Map.of(
            "data", subsectionService.getSubsectionById(subsectionId),
            "success", true));
    }

    @PutMapping("/subsections/{subsectionId}")
    public ResponseEntity<Map<String, Object>> updateSubsection(
        final @PathVariable String subsectionId,
        final @Valid @RequestBody UpdateSubsectionRequest request
    ) {
        return ResponseEntity.ok(Map.of(
            "data", subsectionService.updateSubsection(subsectionId, request),
            "success", true));
    }

    @DeleteMapping("/subsections/{subsectionId}")
    public ResponseEntity<SuccessResponse> deleteSubsection(final @PathVariable String subsectionId) {
        subsectionService.deleteSubsection(subsectionId);
        return ResponseEntity.ok(SuccessResponse.builder()
            .success(true)
            .message(String.format("Подраздел успешно удален id: %s", subsectionId))
            .build());
    }
}
