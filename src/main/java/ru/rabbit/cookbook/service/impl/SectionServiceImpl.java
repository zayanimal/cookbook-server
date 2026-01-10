package ru.rabbit.cookbook.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.rabbit.cookbook.dto.CreateSectionRequest;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.dto.UpdateSectionRequest;
import ru.rabbit.cookbook.entity.SectionEntity;
import ru.rabbit.cookbook.mapper.SectionMapper;
import ru.rabbit.cookbook.repository.SectionRepository;
import ru.rabbit.cookbook.service.SectionService;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final SectionRepository sectionRepository;

    @Override
    public List<Section> getSections() {
        return sectionMapper.toSections(sectionRepository.findAll());
    }

    @Override
    public Section createSection(final CreateSectionRequest request) {
        val section = new SectionEntity();
        section.setTitle(request.getTitle());
        section.setPages(List.of());
        return sectionMapper.toDto(sectionRepository.save(section));
    }

    @Override
    public Section updateSection(final String id, final UpdateSectionRequest request) {
        val section = sectionRepository.findById(id).orElseThrow();
        section.setTitle(request.getTitle());
        return sectionMapper.toDto(sectionRepository.save(section));
    }

    @Override
    public void deleteSection(final String id) {
        sectionRepository.deleteById(id);
    }
}
