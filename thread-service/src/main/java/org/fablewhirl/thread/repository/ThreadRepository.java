package org.fablewhirl.thread.repository;

import org.fablewhirl.thread.entity.ThreadEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends MongoRepository<ThreadEntity, String> {
    List<ThreadEntity> findByUserId(String userId);
}
