package org.fablewhirl.comment.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.mapper.CommentMapper;
import org.fablewhirl.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class ThreadController {
    private final CommentService commentService;
    @PostMapping("/{threadId}/create")
    public ResponseEntity<CommentDto> createComment(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable("threadId") String threadId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(201)
                .body(commentService.createComment(threadId, jwt.getSubject(), commentDto));
    }
    @PostMapping("/{threadId}/reply")
    public ResponseEntity<CommentDto> replyComment(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable("threadId") String threadId,
                                                   @RequestParam String parentId,
                                                   @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(201)
                .body(commentService.replyComment(threadId, parentId, jwt.getSubject(), commentDto));
    }

    @GetMapping("/{threadId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByThreadId(@PathVariable("threadId") String threadId) {
        List<CommentDto> comments = commentService.getAllCommentsByThreadId(threadId);
        return comments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(comments);
    }
}
