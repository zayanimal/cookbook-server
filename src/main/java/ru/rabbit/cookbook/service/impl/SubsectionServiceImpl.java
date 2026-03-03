package ru.rabbit.cookbook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.CreateSubsectionRequest;
import ru.rabbit.cookbook.dto.Subsection;
import ru.rabbit.cookbook.dto.UpdateSubsectionRequest;
import ru.rabbit.cookbook.entity.SubsectionEntity;
import ru.rabbit.cookbook.mapper.PageMapper;
import ru.rabbit.cookbook.mapper.SubsectionMapper;
import ru.rabbit.cookbook.repository.PageRepository;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.repository.SubsectionRepository;
import ru.rabbit.cookbook.service.SubsectionService;

@Service
@RequiredArgsConstructor
public class SubsectionServiceImpl implements SubsectionService {

    private final SubsectionMapper subsectionMapper;

    private final PageMapper pageMapper;

    private final SubsectionRepository subsectionRepository;

    private final SectionRepository sectionRepository;

    private final PageRepository pageRepository;

    @Override
    public List<Subsection> getSubsections(final String sectionId) {
        sectionRepository.findById(sectionId).orElseThrow(() -> new RuntimeException("Section not found"));
        val subsections = subsectionRepository.findBySectionId(sectionId);
        return subsections.stream()
            .map(entity -> {
                val subsection = subsectionMapper.toDto(entity);
                subsection.setPages(pageMapper.toPages(pageRepository.findBySubsectionId(entity.getId())));
                return subsection;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Subsection getSubsectionById(final String subsectionId) {
        val entity = subsectionRepository.findById(subsectionId)
            .orElseThrow(() -> new RuntimeException("Subsection not found"));
        val subsection = subsectionMapper.toDto(entity);
        subsection.setPages(pageMapper.toPages(pageRepository.findBySubsectionId(subsectionId)));
        return subsection;
    }

    @Override
    public Subsection createSubsection(final String sectionId, final CreateSubsectionRequest request) {
        sectionRepository.findById(sectionId).orElseThrow(() -> new RuntimeException("Section not found"));

        val entity = new SubsectionEntity();
        entity.setSectionId(sectionId);
        entity.setTitle(request.getTitle());

        val saved = subsectionRepository.save(entity);
        val subsection = subsectionMapper.toDto(saved);
        subsection.setPages(List.of());
        return subsection;
    }

    @Override
    public Subsection updateSubsection(final String subsectionId, final UpdateSubsectionRequest request) {
        val entity = subsectionRepository.findById(subsectionId)
            .orElseThrow(() -> new RuntimeException("Subsection not found"));
        entity.setTitle(request.getTitle());
        return subsectionMapper.toDto(subsectionRepository.save(entity));
    }

    @Override
    public void deleteSubsection(final String subsectionId) {
        subsectionRepository.findById(subsectionId).orElseThrow(() -> new RuntimeException("Subsection not found"));
        pageRepository.deleteBySubsectionId(subsectionId);
        subsectionRepository.deleteById(subsectionId);
    }
}
