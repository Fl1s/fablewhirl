package org.fablewhirl.comment.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.fablewhirl.comment.mapper.CommentMapper;
import org.fablewhirl.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        entity.setCreatedAt(LocalDateTime.now());

        CommentEntity savedComment = commentRepository.save(entity);
        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public CommentDto replyComment(String threadId, String parentId, String userId, CommentDto commentDto) {
        CommentEntity entity = commentMapper.toEntity(commentDto);
        entity.setThreadId(threadId);
        entity.setParentId(parentId);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());

        CommentEntity savedComment = commentRepository.save(entity);
        return commentMapper.toDto(savedComment);
    }

    public List<CommentDto> getAllCommentsByThreadId(String threadId) {
        List<CommentEntity> comments = commentRepository.findByThreadId(threadId);
        return comments.isEmpty() ? List.of() : comments.stream().map(commentMapper::toDto).toList();
    }

    public List<CommentDto> getAllCommentsByUserId(String userId) {
        List<CommentEntity> comments = commentRepository.findByUserId(userId);
        return comments.isEmpty() ? List.of() : comments.stream().map(commentMapper::toDto).toList();
    }

    public Optional<CommentDto> getCommentById(String commentId) {
        return commentRepository.findById(commentId).map(commentMapper::toDto);
    }

    @Transactional
    public CommentDto updateComment(String commentId, CommentDto commentDto) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("[Comment not found!]"));

        Optional.ofNullable(commentDto.getContent()).ifPresent(comment::setContent);
        comment.setEdited(true);

        CommentEntity updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
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
