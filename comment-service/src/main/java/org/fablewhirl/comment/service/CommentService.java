package org.fablewhirl.comment.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.fablewhirl.comment.mapper.CommentMapper;
import org.fablewhirl.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentDto createComment(String threadId, String userId, CommentDto commentDto) {
        CommentEntity entity = commentMapper.toEntity(commentDto);
        entity.setThreadId(threadId);
        entity.setUserId(userId);

        return commentMapper.toDto(commentRepository.save(entity));
    }

    public List<CommentEntity> getAll() {
        return commentRepository.findAll();
    }
}
