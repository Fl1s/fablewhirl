package org.fablewhirl.thread.service;

import lombok.AllArgsConstructor;
import org.fablewhirl.thread.dto.ThreadCreateDto;
import org.fablewhirl.thread.dto.ThreadReadDto;
import org.fablewhirl.thread.entity.ThreadEntity;
import org.fablewhirl.thread.mapper.ThreadMapper;
import org.fablewhirl.thread.repository.ThreadRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "USER_THREADS_CACHE", key = "#userId")
    public ThreadReadDto createThread(String userId, ThreadCreateDto threadCreateDto) {
        ThreadEntity entity = threadMapper.toEntity(threadCreateDto);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());

        return threadMapper.toDto(threadRepository.save(entity));
    }


    public List<ThreadReadDto> getAllThreads() {
        return threadRepository.findAll().stream()
                .map(threadMapper::toDto)
                .toList();
    }

    @Cacheable(value = "USER_THREADS_CACHE", key = "#userId")
    public List<ThreadReadDto> getAllThreadsByUserId(String userId) {
        return threadRepository.findByUserId(userId).stream()
                .map(threadMapper::toDto)
                .toList();
    }

    @Cacheable(value = "THREAD_CACHE", key = "#threadId")
    public Optional<ThreadReadDto> getThreadById(String threadId) {
        return threadRepository.findById(threadId)
                .map(threadMapper::toDto);
    }

    /*TODO 13.02.2025
    Реализовать связь thread-service и comment-service через асинхронное взаимодействие.
    Необходимо создать обновление счетчика комментариев.
    [Рассмотри возможность реализации через кеширование или событийную архитектуру]
     */

    @Transactional
    @CacheEvict(value = "THREAD_CACHE", key = "#threadId")
    public boolean deleteThread(String threadId) {
        if (threadRepository.existsById(threadId)) {
            threadRepository.deleteById(threadId);
            return true;
        }
        return false;
    }

}
