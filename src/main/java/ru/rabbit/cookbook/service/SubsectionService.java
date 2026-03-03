package ru.rabbit.cookbook.service;

import java.util.List;

import ru.rabbit.cookbook.dto.CreateSubsectionRequest;
import ru.rabbit.cookbook.dto.Subsection;
import ru.rabbit.cookbook.dto.UpdateSubsectionRequest;

public interface SubsectionService {

    List<Subsection> getSubsections(String sectionId);

    Subsection getSubsectionById(String subsectionId);

    Subsection createSubsection(String sectionId, CreateSubsectionRequest request);

    Subsection updateSubsection(String subsectionId, UpdateSubsectionRequest request);

    void deleteSubsection(String subsectionId);
}
