package ru.rabbit.cookbook.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import ru.rabbit.cookbook.dto.Subsection;
import ru.rabbit.cookbook.entity.SubsectionEntity;

@Mapper(componentModel = "spring")
public interface SubsectionMapper {

    Subsection toDto(SubsectionEntity entity);

    List<Subsection> toSubsections(List<SubsectionEntity> entities);
}
