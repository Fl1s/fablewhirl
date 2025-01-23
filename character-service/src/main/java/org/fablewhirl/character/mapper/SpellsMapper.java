package org.fablewhirl.character.mapper;

import org.fablewhirl.character.dto.CharacterDto.SpellsDto;
import org.fablewhirl.character.entity.CharacterEntity.Spells;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface SpellsMapper {
    SpellsDto toDto(Spells entity);

    Spells toEntity(SpellsDto dto);

    void updateEntityFromDto(SpellsDto dto, @MappingTarget Spells entity);
}
