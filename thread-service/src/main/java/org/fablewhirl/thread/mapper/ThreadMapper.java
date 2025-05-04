package org.fablewhirl.thread.mapper;

import org.fablewhirl.thread.dto.ThreadCreateDto;
import org.fablewhirl.thread.dto.ThreadReadDto;
import org.fablewhirl.thread.entity.ThreadEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface ThreadMapper {
    ThreadEntity toEntity(ThreadCreateDto threadEntity);

    ThreadReadDto toDto(ThreadEntity threadEntity);
}
