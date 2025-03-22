package org.fablewhirl.thread.service;

import lombok.AllArgsConstructor;
import org.fablewhirl.thread.dto.ThreadDto;
import org.fablewhirl.thread.entity.ThreadEntity;
import org.fablewhirl.thread.mapper.ThreadMapper;
import org.fablewhirl.thread.repository.ThreadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ThreadService {
    private final ThreadRepository threadRepository;
    private final ThreadMapper threadMapper;

    @Transactional
    public ThreadDto createThread(String userId, ThreadDto threadDto) {
        ThreadEntity entity = threadMapper.toEntity(threadDto);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());

        return threadMapper.toDto(threadRepository.save(entity));
    }

    public List<ThreadDto> getAllThreads() {
        return threadRepository.findAll().stream()
                .map(threadMapper::toDto)
                .toList();
    }

    public List<ThreadDto> getAllThreadsByUserId(String userId) {
        return threadRepository.findByUserId(userId).stream()
                .map(threadMapper::toDto)
                .toList();
    }

    public Optional<ThreadDto> getThreadById(String threadId) {
        return threadRepository.findById(threadId)
                .map(threadMapper::toDto);
    }

    /*TODO 13.02.2025
    Реализовать связь thread-service и comment-service через асинхронное взаимодействие.
    Необходимо создать:
    1. Добавление комментариев в массив comments.
    2. Обновление счетчика комментариев .
    [Рассмотри возможность реализации через кеширование или событийную архитектуру]
     */

    @Transactional
    public boolean deleteThread(String threadId) {
        if (threadRepository.existsById(threadId)) {
            threadRepository.deleteById(threadId);
            return true;
        }
        return false;
    }

}
