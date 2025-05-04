package org.fablewhirl.thread.controller;

import lombok.AllArgsConstructor;
import org.fablewhirl.thread.dto.ThreadCreateDto;
import org.fablewhirl.thread.dto.ThreadReadDto;
import org.fablewhirl.thread.service.ThreadService;
import org.springframework.cache.annotation.CachePut;
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

    @PostMapping
    public ResponseEntity<ThreadReadDto> createThread(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestBody ThreadCreateDto threadCreateDto) {
        return ResponseEntity.status(201)
                .body(threadService.createThread(jwt.getSubject(), threadCreateDto));
    }

    @GetMapping
    public ResponseEntity<List<ThreadReadDto>> getAllThreads() {
        List<ThreadReadDto> threads = threadService.getAllThreads();
        return threads.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(threads);
    }

    @GetMapping("/{threadId}")
    public ResponseEntity<ThreadReadDto> getThreadById(@PathVariable String threadId) {
        return threadService.getThreadById(threadId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<List<ThreadReadDto>> getAllThreadsByUser(@AuthenticationPrincipal Jwt jwt) {
        List<ThreadReadDto> threads = threadService.getAllThreadsByUserId(jwt.getSubject());
        return threads.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(threads);
    }

    @DeleteMapping("/{threadId}")
    public ResponseEntity<Void> deleteThread(@PathVariable String threadId) {
        if (threadService.deleteThread(threadId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

