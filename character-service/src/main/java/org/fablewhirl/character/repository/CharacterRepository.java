package org.fablewhirl.character.repository;

import org.fablewhirl.character.entity.CharacterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends MongoRepository<CharacterEntity, String> {
    List<CharacterEntity> findByUserId(String userId);
}

