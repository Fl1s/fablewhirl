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

    public List<CommentDto> getAllCommentsByThreadId(String threadId) {
        return commentRepository.findByThreadId(threadId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public List<CommentDto> getAllCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public Optional<CommentDto> getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toDto);
    }

    @Transactional
    public CommentDto updateComment(String commentId, CommentDto commentDto) {
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            throw new IllegalArgumentException("[Comment not found!]");
        }
        Optional.ofNullable(commentDto.getContent())
                .ifPresent(comment::setContent);
        Optional.ofNullable(commentDto.getMedia())
                .ifPresent(comment::setMedia);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    public boolean deleteComment(String commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
