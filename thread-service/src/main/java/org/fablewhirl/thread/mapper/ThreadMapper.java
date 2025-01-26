package org.fablewhirl.thread.mapper;

import org.fablewhirl.thread.dto.ThreadDto;
import org.fablewhirl.thread.entity.ThreadEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface ThreadMapper {
    ThreadEntity toEntity(ThreadDto threadEntity);

    ThreadDto toDto(ThreadEntity threadEntity);
}
