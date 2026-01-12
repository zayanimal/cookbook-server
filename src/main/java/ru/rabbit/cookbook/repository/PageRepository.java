package ru.rabbit.cookbook.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rabbit.cookbook.entity.PageEntity;

public interface PageRepository extends MongoRepository<PageEntity, String> {

    List<PageEntity> findBySectionId(String sectionId);

    List<PageEntity> findBySectionIdIn(List<String> sectionIds);

    void deleteBySectionId(String sectionId);
}