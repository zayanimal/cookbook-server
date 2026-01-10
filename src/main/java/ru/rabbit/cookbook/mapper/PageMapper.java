package ru.rabbit.cookbook.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import ru.rabbit.cookbook.dto.Page;
import ru.rabbit.cookbook.entity.PageEntity;

/**
 * Mapper для преобразования PageEntity в Page DTO
 */
@Mapper(componentModel = "spring")
public interface PageMapper {

    /**
     * Преобразует PageEntity в Page DTO
     *
     * @param pageEntity сущность страницы
     * @return DTO страницы
     */
    Page toDto(PageEntity pageEntity);

    List<Page> toPages(List<PageEntity> sectionEntities);
}
