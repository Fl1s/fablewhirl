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
@RequestMapping("/api/v1/threads")
@RequiredArgsConstructor
public class ThreadController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Transactional
    @PostMapping("/{threadId}/comments")
    public ResponseEntity<CommentDto> createComment(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable("threadId") String threadId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(201)
                .body(commentService.createComment(threadId, jwt.getSubject(), commentDto));
    }

    @GetMapping("/{threadId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByThreadId(@PathVariable("threadId") String threadId) {
        List<CommentDto> comments = commentService.getAllCommentsByThreadId(threadId)
                .stream().map(commentMapper::toDto)
                .toList();
        return comments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(comments);
    }
}
