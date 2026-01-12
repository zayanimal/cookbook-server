package ru.rabbit.cookbook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.CreateSectionRequest;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.dto.UpdateSectionRequest;
import ru.rabbit.cookbook.entity.SectionEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.mapper.SectionMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.service.SectionService;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final PageMapper pageMapper;

    private final PageRepository pageRepository;

    private final SectionRepository sectionRepository;

    @Override
    public List<Section> getSections() {
        val sections = sectionRepository.findAll();
        return sections.stream()
            .map(sectionEntity -> {
                val section = sectionMapper.toDto(sectionEntity);
                val pages = pageRepository.findBySectionId(sectionEntity.getId());
                section.setPages(pageMapper.toPages(pages));
                return section;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Section createSection(final CreateSectionRequest request) {
        val section = new SectionEntity();
        section.setTitle(request.getTitle());

        val sectionDto = sectionMapper.toDto(sectionRepository.save(section));
        sectionDto.setPages(List.of());

        return sectionDto;
    }

    @Override
    public Section updateSection(final String id, final UpdateSectionRequest request) {
        val section = sectionRepository.findById(id).orElseThrow();
        section.setTitle(request.getTitle());
        return sectionMapper.toDto(sectionRepository.save(section));
    }

    @Override
    public void deleteSection(final String id) {
        sectionRepository.findById(id).orElseThrow();
        pageRepository.deleteBySectionId(id);
        sectionRepository.deleteById(id);
    }
}
