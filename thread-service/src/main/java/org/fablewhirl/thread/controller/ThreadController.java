package org.fablewhirl.thread.controller;

import lombok.AllArgsConstructor;
import org.fablewhirl.thread.dto.ThreadDto;
import org.fablewhirl.thread.mapper.ThreadMapper;
import org.fablewhirl.thread.service.ThreadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/threads")
@AllArgsConstructor
public class ThreadController {
    private final ThreadService threadService;
    private final ThreadMapper threadMapper;

    @PostMapping("/users/{userId}/post")
    public ResponseEntity<ThreadDto> createThread(@PathVariable String userId, @RequestBody ThreadDto threadDto) {
        return ResponseEntity.ok(threadService.createThread(userId, threadDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ThreadDto>> getAllThreads() {
        List<ThreadDto> users = threadService.getAllThreads().stream()
                .map(threadMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ThreadDto>> getAllThreadsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(threadService.getAllThreadsByUserId(userId));
    }

    @GetMapping("/{threadId}")
    public ResponseEntity<ThreadDto> getThread(@PathVariable String threadId) {
        return threadService.getThreadById(threadId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{threadId}")
    public ResponseEntity<ThreadDto> updateThread(@PathVariable String threadId, @RequestBody ThreadDto threadDto) {
        return ResponseEntity.ok(threadService.updateThread(threadId, threadDto));
    }

    @DeleteMapping("/{threadId}")
    public ResponseEntity<Void> deleteThread(@PathVariable String threadId) {
        if (threadService.deleteThread(threadId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
