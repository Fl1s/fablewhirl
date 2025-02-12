package org.fablewhirl.comment.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.mapper.CommentMapper;
import org.fablewhirl.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("commentId") String commentId) {
        return commentService.getCommentById(commentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@AuthenticationPrincipal Jwt jwt) {
        List<CommentDto> comments = commentService.getAllCommentsByUserId(jwt.getSubject())
                .stream().map(commentMapper::toDto)
                .toList();
        return comments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(comments);
    }

    @Transactional
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String commentId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDto));
    }

    @Transactional
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable String commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
