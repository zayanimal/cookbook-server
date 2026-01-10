package ru.rabbit.cookbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rabbit.cookbook.entity.SectionEntity;

public interface SectionRepository extends MongoRepository<SectionEntity, String> {

}
