package org.fablewhirl.comment.mapper;

import org.fablewhirl.comment.dto.CommentDto;
import org.fablewhirl.comment.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface CommentMapper {
    CommentEntity toEntity(CommentDto dto);

    CommentDto toDto(CommentEntity entity);

    void updateEntityFromDto(CommentDto dto, @MappingTarget CommentEntity entity);

}
