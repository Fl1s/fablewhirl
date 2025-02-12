package org.fablewhirl.comment.repository;

import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    List<CommentEntity> findByThreadId(String threadId);

    List<CommentEntity> findByUserId(String userId);
}
