package org.fablewhirl.character.mapper;

import org.fablewhirl.character.dto.CharacterDto.DisabledBlocksDto;
import org.fablewhirl.character.entity.CharacterEntity.DisabledBlocks;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface DisabledBlocksMapper {
    DisabledBlocksDto toDto(DisabledBlocks entity);

    DisabledBlocks toEntity(DisabledBlocksDto dto);

    void updateEntityFromDto(DisabledBlocksDto dto, @MappingTarget DisabledBlocks entity);
}
