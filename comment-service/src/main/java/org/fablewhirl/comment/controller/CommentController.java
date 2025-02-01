package org.fablewhirl.comment.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.fablewhirl.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments/")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/threads/{threadId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable("threadId") String threadId,
                                                    @PathVariable("userId") String userId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(threadId, userId, commentDto));
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        return ResponseEntity.ok(commentService.getAll());
    }

    


}
