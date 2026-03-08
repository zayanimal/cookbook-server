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
import ru.rabbit.cookbook.mapper.SubsectionMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;
import ru.rabbit.cookbook.service.SectionService;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final SubsectionMapper subsectionMapper;

    private final PageMapper pageMapper;

    private final SectionRepository sectionRepository;

    private final SubsectionRepository subsectionRepository;

    private final PageRepository pageRepository;

    @Override
    public List<Section> getSections() {
        val sections = sectionRepository.findAll();
        return sections.stream()
            .map(sectionEntity -> {
                val section = sectionMapper.toDto(sectionEntity);
                val subsectionEntities = subsectionRepository.findBySectionId(sectionEntity.getId());
                val subsections = subsectionEntities.stream()
                    .map(subsectionEntity -> {
                        val subsection = subsectionMapper.toDto(subsectionEntity);
                        subsection.setPages(pageMapper.toPages(pageRepository.findBySubsectionId(subsectionEntity.getId())));
                        return subsection;
                    })
                    .collect(Collectors.toList());
                section.setSubsections(subsections);
                return section;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Section getSectionById(final String id) {
        val sectionEntity = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        val section = sectionMapper.toDto(sectionEntity);
        val subsectionEntities = subsectionRepository.findBySectionId(id);
        val subsections = subsectionEntities.stream()
            .map(subsectionEntity -> {
                val subsection = subsectionMapper.toDto(subsectionEntity);
                subsection.setPages(pageMapper.toPages(pageRepository.findBySubsectionId(subsectionEntity.getId())));
                return subsection;
            })
            .collect(Collectors.toList());
        section.setSubsections(subsections);
        return section;
    }

    @Override
    public Section createSection(final CreateSectionRequest request) {
        val section = new SectionEntity();
        section.setTitle(request.getTitle());

        val sectionDto = sectionMapper.toDto(sectionRepository.save(section));
        sectionDto.setSubsections(List.of());

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
        val subsections = subsectionRepository.findBySectionId(id);
        subsections.forEach(subsection -> pageRepository.deleteBySubsectionId(subsection.getId()));
        subsectionRepository.deleteBySectionId(id);
        sectionRepository.deleteById(id);
    }
}
