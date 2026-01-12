package ru.rabbit.cookbook.mapper;

import org.mapstruct.Mapper;
import ru.rabbit.cookbook.dto.Section;
import ru.rabbit.cookbook.entity.SectionEntity;

/**
 * Mapper для преобразования SectionEntity в Section DTO
 */
@Mapper(componentModel = "spring")
public interface SectionMapper {

    /**
     * Преобразует SectionEntity в Section DTO
     *
     * @param sectionEntity сущность раздела
     * @return DTO раздела
     */
    Section toDto(SectionEntity sectionEntity);
}
