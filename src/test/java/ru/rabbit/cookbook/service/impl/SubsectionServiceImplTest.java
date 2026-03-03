package ru.rabbit.cookbook.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rabbit.cookbook.dto.CreateSubsectionRequest;
import ru.rabbit.cookbook.dto.Subsection;
import ru.rabbit.cookbook.dto.UpdateSubsectionRequest;
import ru.rabbit.cookbook.entity.SectionEntity;
import ru.rabbit.cookbook.entity.SubsectionEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.mapper.SubsectionMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;

@ExtendWith(MockitoExtension.class)
class SubsectionServiceImplTest {

    @Mock
    private SubsectionMapper subsectionMapper;

    @Mock
    private PageMapper pageMapper;

    @Mock
    private SubsectionRepository subsectionRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private PageRepository pageRepository;

    @InjectMocks
    private SubsectionServiceImpl subsectionService;

    @Test
    @DisplayName("Получение подразделов — бросает исключение если раздел не найден")
    void getSubsections_throwsWhenSectionNotFound() {
        when(sectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subsectionService.getSubsections("missing"));
    }

    @Test
    @DisplayName("Получение подразделов — возвращает подразделы со страницами")
    void getSubsections_returnsSubsectionsWithPages() {
        val entity = new SubsectionEntity();
        entity.setId("sub1");
        entity.setSectionId("sec1");

        val subsection = new Subsection();
        subsection.setId("sub1");

        when(sectionRepository.findById("sec1")).thenReturn(Optional.of(new SectionEntity()));
        when(subsectionRepository.findBySectionId("sec1")).thenReturn(List.of(entity));
        when(subsectionMapper.toDto(entity)).thenReturn(subsection);
        when(pageRepository.findBySubsectionId("sub1")).thenReturn(List.of());
        when(pageMapper.toPages(anyList())).thenReturn(List.of());

        val result = subsectionService.getSubsections("sec1");

        assertEquals(1, result.size());
        assertEquals("sub1", result.get(0).getId());
        assertTrue(result.get(0).getPages().isEmpty());
    }

    @Test
    @DisplayName("Получение подраздела по ID — возвращает подраздел со страницами")
    void getSubsectionById_returnsSubsectionWithPages() {
        val entity = new SubsectionEntity();
        entity.setId("sub1");

        val subsection = new Subsection();
        subsection.setId("sub1");

        when(subsectionRepository.findById("sub1")).thenReturn(Optional.of(entity));
        when(subsectionMapper.toDto(entity)).thenReturn(subsection);
        when(pageRepository.findBySubsectionId("sub1")).thenReturn(List.of());
        when(pageMapper.toPages(anyList())).thenReturn(List.of());

        val result = subsectionService.getSubsectionById("sub1");

        assertEquals("sub1", result.getId());
        assertTrue(result.getPages().isEmpty());
    }

    @Test
    @DisplayName("Получение подраздела по ID — бросает исключение если подраздел не найден")
    void getSubsectionById_throwsWhenNotFound() {
        when(subsectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subsectionService.getSubsectionById("missing"));
    }

    @Test
    @DisplayName("Создание подраздела — бросает исключение если раздел не найден")
    void createSubsection_throwsWhenSectionNotFound() {
        when(sectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> subsectionService.createSubsection("missing", new CreateSubsectionRequest("title"))
        );
    }

    @Test
    @DisplayName("Создание подраздела — возвращает подраздел с пустым списком страниц")
    void createSubsection_returnsSubsectionWithEmptyPages() {
        val request = new CreateSubsectionRequest("New Subsection");
        val saved = new SubsectionEntity();
        saved.setId("sub1");
        saved.setSectionId("sec1");
        saved.setTitle("New Subsection");

        val subsection = new Subsection();
        subsection.setId("sub1");
        subsection.setTitle("New Subsection");

        when(sectionRepository.findById("sec1")).thenReturn(Optional.of(new SectionEntity()));
        when(subsectionRepository.save(any(SubsectionEntity.class))).thenReturn(saved);
        when(subsectionMapper.toDto(saved)).thenReturn(subsection);

        val result = subsectionService.createSubsection("sec1", request);

        assertEquals("sub1", result.getId());
        assertTrue(result.getPages().isEmpty());
    }

    @Test
    @DisplayName("Обновление подраздела — изменяет заголовок")
    void updateSubsection_updatesTitle() {
        val request = new UpdateSubsectionRequest("Updated Title");
        val entity = new SubsectionEntity();
        entity.setId("sub1");
        entity.setTitle("Old Title");

        val updated = new SubsectionEntity();
        updated.setId("sub1");
        updated.setTitle("Updated Title");

        val expected = new Subsection();
        expected.setId("sub1");
        expected.setTitle("Updated Title");

        when(subsectionRepository.findById("sub1")).thenReturn(Optional.of(entity));
        when(subsectionRepository.save(entity)).thenReturn(updated);
        when(subsectionMapper.toDto(updated)).thenReturn(expected);

        val result = subsectionService.updateSubsection("sub1", request);

        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    @DisplayName("Обновление подраздела — бросает исключение если подраздел не найден")
    void updateSubsection_throwsWhenNotFound() {
        when(subsectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> subsectionService.updateSubsection("missing", new UpdateSubsectionRequest("t"))
        );
    }

    @Test
    @DisplayName("Удаление подраздела — сначала удаляет страницы, затем подраздел")
    void deleteSubsection_deletesPagesThenSubsection() {
        val entity = new SubsectionEntity();
        entity.setId("sub1");

        when(subsectionRepository.findById("sub1")).thenReturn(Optional.of(entity));

        subsectionService.deleteSubsection("sub1");

        verify(pageRepository).deleteBySubsectionId("sub1");
        verify(subsectionRepository).deleteById("sub1");
    }

    @Test
    @DisplayName("Удаление подраздела — бросает исключение если подраздел не найден")
    void deleteSubsection_throwsWhenNotFound() {
        when(subsectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subsectionService.deleteSubsection("missing"));
    }
}
