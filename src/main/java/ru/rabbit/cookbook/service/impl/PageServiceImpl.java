package ru.rabbit.cookbook.service.impl;

import static java.util.Objects.nonNull;

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
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.service.PageService;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageMapper pageMapper;

    private final SectionRepository sectionRepository;

    @Override
    public List<Page> getPages(final String sectionId) {
        val pages = sectionRepository.findById(sectionId).orElseThrow();
        return pageMapper.toPages(pages.getPages());
    }

    @Override
    public Page createPage(final String sectionId, final CreatePageRequest request) {
        val section = sectionRepository.findById(sectionId).orElseThrow();

        val page = new PageEntity();
        page.setId(String.valueOf(section.getPages().size() + 1));
        page.setTitle(request.getTitle());
        page.setContent(getContent(request.getContent()));

        section.getPages().add(page);

        sectionRepository.save(section);

        return pageMapper.toDto(page);
    }

    @Override
    public Page updatePage(final PageUpdateParams params) {
        val section = sectionRepository.findById(params.getSectionId()).orElseThrow();
        val page = section.getPages().stream()
            .filter(p -> p.getId().equals(params.getPageId()))
            .findFirst()
            .orElseThrow();

        val request = params.getRequest();
        val title = request.getTitle();
        val content = request.getContent();

        if (nonNull(title)) {
            page.setTitle(request.getTitle());
        }

        if (nonNull(content)) {
            page.setContent(getContent(content));
        }

        sectionRepository.save(section);

        return pageMapper.toDto(page);
    }

    public void deletePage(final String sectionId, final String pageId) {
        val section = sectionRepository.findById(sectionId).orElseThrow();
        section.setPages(section.getPages().stream()
            .filter(p -> !p.getId().equals(pageId))
            .toList());

        sectionRepository.save(section);
    }

    private EditorJSContent getContent(final EditorJSContent content) {
        return nonNull(content)
            ? content
            : EditorJSContent.builder()
                .blocks(List.of())
                .build();
    }
}
