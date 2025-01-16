package org.fablewhirl.user.repository;

import org.fablewhirl.user.entity.UserMediaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMediaRepository extends MongoRepository<UserMediaEntity, String> {
    Optional<UserMediaEntity> findByUserId(Long userId);
}

