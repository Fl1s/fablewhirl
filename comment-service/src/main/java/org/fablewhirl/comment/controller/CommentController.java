package org.fablewhirl.comment.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
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

    @PostMapping("/{threadId}")
    public ResponseEntity<CommentDto> createComment(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable("threadId") String threadId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(201)
                .body(commentService.createComment(threadId, jwt.getSubject(), commentDto));
    }

    @PostMapping("/{threadId}/{parentId}/reply")
    public ResponseEntity<CommentDto> replyToComment(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable("threadId") String threadId,
                                                     @PathVariable("parentId") String parentId,
                                                     @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(201)
                .body(commentService.replyComment(threadId, parentId, jwt.getSubject(), commentDto));
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<List<CommentDto>> getCommentsByThreadId(@PathVariable("threadId") String threadId) {
        List<CommentDto> comments = commentService.getAllCommentsByThreadId(threadId);
        return comments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("commentId") String commentId) {
        return commentService.getCommentById(commentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@AuthenticationPrincipal Jwt jwt) {
        List<CommentDto> comments = commentService.getAllCommentsByUserId(jwt.getSubject());
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
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

