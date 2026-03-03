package ru.rabbit.cookbook.service.impl;

import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.CreatePageRequest;
import ru.rabbit.cookbook.dto.EditorJSContent;
import ru.rabbit.cookbook.dto.Page;
import ru.rabbit.cookbook.dto.PageUpdateParams;
import ru.rabbit.cookbook.entity.PageEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;
import ru.rabbit.cookbook.service.PageService;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageMapper pageMapper;

    private final PageRepository pageRepository;

    private final SubsectionRepository subsectionRepository;

    @Override
    public List<Page> getPages(final String subsectionId) {
        val pages = pageRepository.findBySubsectionId(subsectionId);
        return pageMapper.toPages(pages);
    }

    @Override
    public Page createPage(final String subsectionId, final CreatePageRequest request) {
        subsectionRepository.findById(subsectionId).orElseThrow(() -> new RuntimeException("Subsection not found"));

        val now = Instant.now().toString();
        val page = new PageEntity();
        page.setSubsectionId(subsectionId);
        page.setTitle(request.getTitle());
        page.setContent(getContent(request.getContent()));
        page.setCreatedAt(now);
        page.setUpdatedAt(now);

        val savedPage = pageRepository.save(page);
        return pageMapper.toDto(savedPage);
    }

    @Override
    public Page updatePage(final PageUpdateParams params) {
        val page = pageRepository.findById(params.getPageId())
            .orElseThrow(() -> new RuntimeException("Page not found"));

        val request = params.getRequest();
        val title = request.getTitle();
        val content = request.getContent();

        if (nonNull(title)) {
            page.setTitle(title);
        }

        if (nonNull(content)) {
            page.setContent(getContent(content));
        }

        page.setUpdatedAt(Instant.now().toString());

        val updatedPage = pageRepository.save(page);
        return pageMapper.toDto(updatedPage);
    }

    @Override
    public void deletePage(final String pageId) {
        pageRepository.deleteById(pageId);
    }

    private EditorJSContent getContent(final EditorJSContent content) {
        return nonNull(content)
            ? content
            : EditorJSContent.builder()
                .blocks(List.of())
                .build();
    }
}
