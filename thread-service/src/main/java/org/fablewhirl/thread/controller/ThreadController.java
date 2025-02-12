package org.fablewhirl.thread.controller;

import lombok.AllArgsConstructor;
import org.fablewhirl.thread.dto.ThreadDto;
import org.fablewhirl.thread.mapper.ThreadMapper;
import org.fablewhirl.thread.service.ThreadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/threads")
@AllArgsConstructor
public class ThreadController {
    private final ThreadService threadService;
    private final ThreadMapper threadMapper;

    @PostMapping
    public ResponseEntity<ThreadDto> createThread(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestBody ThreadDto threadDto) {
        return ResponseEntity.status(201)
                .body(threadService.createThread(jwt.getSubject(), threadDto));
    }

    @GetMapping
    public ResponseEntity<List<ThreadDto>> getAllThreads() {
        List<ThreadDto> threads = threadService.getAllThreads().stream()
                .map(threadMapper::toDto)
                .toList();
        return threads.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(threads);
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<ThreadDto>> getAllThreadsByUserId(@AuthenticationPrincipal Jwt jwt){
        List<ThreadDto> threads = threadService.getAllThreadsByUserId(jwt.getSubject())
                .stream().map(threadMapper::toDto)
                .toList();
        return threads.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(threads);
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

