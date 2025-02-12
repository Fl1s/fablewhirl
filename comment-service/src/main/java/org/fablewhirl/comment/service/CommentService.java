package org.fablewhirl.comment.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.apache.logging.log4j.util.Lazy;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.fablewhirl.comment.mapper.CommentMapper;
import org.fablewhirl.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public List<CommentEntity> getAllCommentsByThreadId(String threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    public List<CommentEntity> getAllCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId);
    }

    public Optional<CommentDto> getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toDto);
    }

    public CommentDto updateComment(String commentId, CommentDto commentDto) {
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            throw new IllegalArgumentException("[Comment not found!]");
        }
        comment.setContent(commentDto.getContent());
        comment.setMedia(commentDto.getMedia());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    public boolean deleteComment(String commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
