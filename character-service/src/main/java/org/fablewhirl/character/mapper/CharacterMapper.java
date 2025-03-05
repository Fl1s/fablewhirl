package org.fablewhirl.character.mapper;

import org.fablewhirl.character.dto.CharacterDto;
import org.fablewhirl.character.entity.CharacterEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface CharacterMapper {
    CharacterDto toDto(CharacterEntity entity);

    CharacterEntity toEntity(CharacterDto dto);

    void updateEntityFromDto(CharacterDto userCreateEditDto, @MappingTarget CharacterEntity userEntity);

    @AfterMapping
    default void mapDisabledBlocksId(CharacterEntity entity, @MappingTarget CharacterDto dto) {
        if (entity.getDisabledBlocks() != null && dto.getDisabledBlocks() != null) {
            dto.getDisabledBlocks().setId(entity.getId());
        }
    }
}

