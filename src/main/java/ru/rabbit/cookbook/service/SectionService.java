package ru.rabbit.cookbook.service;

import java.util.List;

import ru.rabbit.cookbook.dto.CreateSectionRequest;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.dto.UpdateSectionRequest;

public interface SectionService {

    List<Section> getSections();

    Section createSection(CreateSectionRequest request);

    Section updateSection(String id, UpdateSectionRequest request);

    void deleteSection(String id);
}
