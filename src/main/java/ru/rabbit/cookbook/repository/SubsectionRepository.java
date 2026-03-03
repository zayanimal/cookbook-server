package ru.rabbit.cookbook.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rabbit.cookbook.entity.SubsectionEntity;

public interface SubsectionRepository extends MongoRepository<SubsectionEntity, String> {

    List<SubsectionEntity> findBySectionId(String sectionId);

    void deleteBySectionId(String sectionId);
}
