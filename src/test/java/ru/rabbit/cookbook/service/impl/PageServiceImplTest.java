package ru.rabbit.cookbook.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rabbit.cookbook.dto.CreatePageRequest;
import ru.rabbit.cookbook.dto.Page;
import ru.rabbit.cookbook.dto.PageUpdateParams;
import ru.rabbit.cookbook.dto.UpdatePageRequest;
import ru.rabbit.cookbook.entity.PageEntity;
import ru.rabbit.cookbook.entity.SubsectionEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;
import ru.rabbit.cookbook.util.MarkdownSanitizer;

@ExtendWith(MockitoExtension.class)
class PageServiceImplTest {

    @Mock
    private PageMapper pageMapper;

    @Mock
    private PageRepository pageRepository;

    @Mock
    private SubsectionRepository subsectionRepository;

    @Mock
    private MarkdownSanitizer markdownSanitizer;

    @InjectMocks
    private PageServiceImpl pageService;

    @BeforeEach
    void setupSanitizer() {
        Mockito.lenient().when(markdownSanitizer.sanitize(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    @DisplayName("Получение страниц — возвращает маппированные страницы")
    void getPages_returnsMappedPages() {
        val entity = new PageEntity();
        entity.setId("p1");

        val page = new Page();
        page.setId("p1");

        when(pageRepository.findBySubsectionId("sub1")).thenReturn(List.of(entity));
        when(pageMapper.toPages(List.of(entity))).thenReturn(List.of(page));

        val result = pageService.getPages("sub1");

        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).getId());
    }

    @Test
    @DisplayName("Создание страницы — бросает исключение если подраздел не найден")
    void createPage_throwsWhenSubsectionNotFound() {
        when(subsectionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> pageService.createPage("missing", new CreatePageRequest("title", null))
        );
    }

    @Test
    @DisplayName("Создание страницы — устанавливает пустую строку если контент не передан")
    void createPage_withNullContent_setsEmptyString() {
        val request = new CreatePageRequest("New Page", null);
        val savedEntity = new PageEntity();
        savedEntity.setId("p1");
        savedEntity.setTitle("New Page");
        savedEntity.setContent("");

        val expected = new Page();
        expected.setId("p1");
        expected.setTitle("New Page");
        expected.setContent("");

        when(subsectionRepository.findById("sub1")).thenReturn(Optional.of(new SubsectionEntity()));
        when(pageRepository.save(any(PageEntity.class))).thenReturn(savedEntity);
        when(pageMapper.toDto(savedEntity)).thenReturn(expected);

        val result = pageService.createPage("sub1", request);

        assertEquals("p1", result.getId());
        verify(pageRepository).save(any(PageEntity.class));
    }

    @Test
    @DisplayName("Создание страницы — сохраняет переданный Markdown контент")
    void createPage_withContent_preservesContent() {
        val content = "## Заголовок\n\nТекст страницы";
        val request = new CreatePageRequest("New Page", content);
        val savedEntity = new PageEntity();
        savedEntity.setId("p1");
        savedEntity.setContent(content);

        val expected = new Page();
        expected.setId("p1");
        expected.setContent(content);

        when(subsectionRepository.findById("sub1")).thenReturn(Optional.of(new SubsectionEntity()));
        when(pageRepository.save(any(PageEntity.class))).thenReturn(savedEntity);
        when(pageMapper.toDto(savedEntity)).thenReturn(expected);

        val result = pageService.createPage("sub1", request);

        assertEquals(content, result.getContent());
    }

    @Test
    @DisplayName("Обновление страницы — бросает исключение если страница не найдена")
    void updatePage_throwsWhenNotFound() {
        when(pageRepository.findById("missing")).thenReturn(Optional.empty());

        val params = PageUpdateParams.builder()
            .pageId("missing")
            .request(new UpdatePageRequest("title", null))
            .build();

        assertThrows(RuntimeException.class, () -> pageService.updatePage(params));
    }

    @Test
    @DisplayName("Обновление страницы — изменяет только ненулевые поля")
    void updatePage_updatesOnlyNonNullTitle() {
        val entity = new PageEntity();
        entity.setId("p1");
        entity.setTitle("Old Title");

        val request = new UpdatePageRequest("New Title", null);
        val params = PageUpdateParams.builder().pageId("p1").request(request).build();

        val savedEntity = new PageEntity();
        savedEntity.setId("p1");
        savedEntity.setTitle("New Title");

        val expected = new Page();
        expected.setId("p1");
        expected.setTitle("New Title");

        when(pageRepository.findById("p1")).thenReturn(Optional.of(entity));
        when(pageRepository.save(entity)).thenReturn(savedEntity);
        when(pageMapper.toDto(savedEntity)).thenReturn(expected);

        val result = pageService.updatePage(params);

        assertEquals("New Title", result.getTitle());
        verify(pageRepository).save(entity);
    }

    @Test
    @DisplayName("Обновление страницы — изменяет все поля при полном запросе")
    void updatePage_updatesAllFields() {
        val entity = new PageEntity();
        entity.setId("p1");
        entity.setTitle("Old Title");

        val content = "## Заголовок\n\nОбновлённый текст";
        val request = new UpdatePageRequest("New Title", content);
        val params = PageUpdateParams.builder().pageId("p1").request(request).build();

        val savedEntity = new PageEntity();
        savedEntity.setId("p1");
        savedEntity.setTitle("New Title");
        savedEntity.setContent(content);

        val expected = new Page();
        expected.setId("p1");
        expected.setTitle("New Title");
        expected.setContent(content);

        when(pageRepository.findById("p1")).thenReturn(Optional.of(entity));
        when(pageRepository.save(entity)).thenReturn(savedEntity);
        when(pageMapper.toDto(savedEntity)).thenReturn(expected);

        val result = pageService.updatePage(params);

        assertEquals("New Title", result.getTitle());
        assertEquals(content, result.getContent());
    }

    @Test
    @DisplayName("Удаление страницы — вызывает удаление по ID в репозитории")
    void deletePage_callsRepositoryDeleteById() {
        pageService.deletePage("p1");

        verify(pageRepository).deleteById("p1");
    }
}
