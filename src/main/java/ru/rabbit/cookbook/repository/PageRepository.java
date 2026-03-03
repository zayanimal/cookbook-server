package ru.rabbit.cookbook.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rabbit.cookbook.entity.PageEntity;

public interface PageRepository extends MongoRepository<PageEntity, String> {

    List<PageEntity> findBySubsectionId(String subsectionId);

    void deleteBySubsectionId(String subsectionId);
}
