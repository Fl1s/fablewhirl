package org.fablewhirl.thread.service;

import jakarta.persistence.EntityNotFoundException;
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

        return threadMapper.toDto(threadRepository.save(entity));
    }

    public List<ThreadEntity> getAllThreads() {
        return threadRepository.findAll();
    }

    public List<ThreadDto> getAllThreadsByUserId(String userId) {
        List<ThreadEntity> threads = threadRepository.findByUserId(userId);
        return threads.stream()
                .map(threadMapper::toDto)
                .toList();
    }

    public Optional<ThreadDto> getThreadById(String threadId) {
        return threadRepository.findById(threadId)
                .map(threadMapper::toDto);
    }

    @Transactional
    public ThreadDto updateThread(String threadId, ThreadDto threadDto) {
        ThreadEntity entity = threadRepository.findById(threadId).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(threadId);
        }

        Optional.ofNullable(threadDto.getUserId())
                .ifPresent(entity::setUserId);
        Optional.ofNullable(threadDto.getTitle())
                .ifPresent(entity::setTitle);
        Optional.ofNullable(threadDto.getContent())
                .ifPresent(entity::setContent);
        Optional.ofNullable(threadDto.getMedia())
                .ifPresent(entity::setMedia);
        Optional.ofNullable(threadDto.getComments())
                .ifPresent(entity::setComments);
        Optional.of(threadDto.getCommentCount())
                .ifPresent(entity::setCommentCount);

        return threadMapper.toDto(threadRepository.save(entity));
    }

    @Transactional
    public boolean deleteThread(String threadId) {
        if (threadRepository.existsById(threadId)) {
            threadRepository.deleteById(threadId);
            return true;
        }
        return false;
    }

}
