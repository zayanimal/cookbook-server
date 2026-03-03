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
import ru.rabbit.cookbook.dto.CreateSectionRequest;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.dto.Subsection;
import ru.rabbit.cookbook.dto.UpdateSectionRequest;
import ru.rabbit.cookbook.entity.SectionEntity;
import ru.rabbit.cookbook.entity.SubsectionEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.mapper.SectionMapper;
import ru.rabbit.cookbook.mapper.SubsectionMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionServiceImplTest {

    @Mock
    private SectionMapper sectionMapper;

    @Mock
    private SubsectionMapper subsectionMapper;

    @Mock
    private PageMapper pageMapper;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SubsectionRepository subsectionRepository;

    @Mock
    private PageRepository pageRepository;

    @InjectMocks
    private SectionServiceImpl sectionService;

    @Test
    @DisplayName("Получение разделов — возвращает разделы с подразделами и страницами")
    void getSections_returnsSectionsWithSubsectionsAndPages() {
        val sectionEntity = new SectionEntity();
        sectionEntity.setId("s1");
        sectionEntity.setTitle("Section 1");

        val subsectionEntity = new SubsectionEntity();
        subsectionEntity.setId("sub1");
        subsectionEntity.setSectionId("s1");

        val section = new Section();
        section.setId("s1");
        section.setTitle("Section 1");

        val subsection = new Subsection();
        subsection.setId("sub1");

        when(sectionRepository.findAll()).thenReturn(List.of(sectionEntity));
        when(sectionMapper.toDto(sectionEntity)).thenReturn(section);
        when(subsectionRepository.findBySectionId("s1")).thenReturn(List.of(subsectionEntity));
        when(subsectionMapper.toDto(subsectionEntity)).thenReturn(subsection);
        when(pageRepository.findBySubsectionId("sub1")).thenReturn(List.of());
        when(pageMapper.toPages(anyList())).thenReturn(List.of());

        val result = sectionService.getSections();

        assertEquals(1, result.size());
        assertEquals("s1", result.get(0).getId());
        assertEquals(1, result.get(0).getSubsections().size());
    }

    @Test
    @DisplayName("Создание раздела — возвращает раздел с пустым списком подразделов")
    void createSection_returnsSectionWithEmptySubsections() {
        val request = new CreateSectionRequest("New Section");
        val savedEntity = new SectionEntity();
        savedEntity.setId("s2");
        savedEntity.setTitle("New Section");

        val section = new Section();
        section.setId("s2");
        section.setTitle("New Section");

        when(sectionRepository.save(any(SectionEntity.class))).thenReturn(savedEntity);
        when(sectionMapper.toDto(savedEntity)).thenReturn(section);

        val result = sectionService.createSection(request);

        assertEquals("s2", result.getId());
        assertTrue(result.getSubsections().isEmpty());
    }

    @Test
    @DisplayName("Обновление раздела — изменяет заголовок")
    void updateSection_updatesTitle() {
        val request = new UpdateSectionRequest("Updated Title");
        val entity = new SectionEntity();
        entity.setId("s1");
        entity.setTitle("Old Title");

        val updated = new SectionEntity();
        updated.setId("s1");
        updated.setTitle("Updated Title");

        val expected = new Section();
        expected.setId("s1");
        expected.setTitle("Updated Title");

        when(sectionRepository.findById("s1")).thenReturn(Optional.of(entity));
        when(sectionRepository.save(entity)).thenReturn(updated);
        when(sectionMapper.toDto(updated)).thenReturn(expected);

        val result = sectionService.updateSection("s1", request);

        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    @DisplayName("Обновление раздела — бросает исключение если раздел не найден")
    void updateSection_throwsWhenNotFound() {
        when(sectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> sectionService.updateSection("missing", new UpdateSectionRequest("t"))
        );
    }

    @Test
    @DisplayName("Удаление раздела — каскадно удаляет подразделы и страницы")
    void deleteSection_cascadeDeletesSubsectionsAndPages() {
        val entity = new SectionEntity();
        entity.setId("s1");

        val sub1 = new SubsectionEntity();
        sub1.setId("sub1");

        val sub2 = new SubsectionEntity();
        sub2.setId("sub2");

        when(sectionRepository.findById("s1")).thenReturn(Optional.of(entity));
        when(subsectionRepository.findBySectionId("s1")).thenReturn(List.of(sub1, sub2));

        sectionService.deleteSection("s1");

        verify(pageRepository).deleteBySubsectionId("sub1");
        verify(pageRepository).deleteBySubsectionId("sub2");
        verify(subsectionRepository).deleteBySectionId("s1");
        verify(sectionRepository).deleteById("s1");
    }

    @Test
    @DisplayName("Удаление раздела — бросает исключение если раздел не найден")
    void deleteSection_throwsWhenNotFound() {
        when(sectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sectionService.deleteSection("missing"));
    }
}
