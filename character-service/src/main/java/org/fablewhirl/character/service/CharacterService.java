package org.fablewhirl.character.service;

import io.micrometer.observation.ObservationFilter;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.character.dto.CharacterDto;
import org.fablewhirl.character.entity.CharacterEntity;
import org.fablewhirl.character.mapper.CharacterMapper;
import org.fablewhirl.character.mapper.DisabledBlocksMapper;
import org.fablewhirl.character.mapper.SpellsMapper;
import org.fablewhirl.character.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    private final DisabledBlocksMapper disabledBlocksMapper;
    private final SpellsMapper spellsMapper;

    @Transactional
    public void createCharacter(String userId,CharacterDto characterDto) {
        CharacterEntity character = characterMapper.toEntity(characterDto);
        character.setUserId(userId);
        characterRepository.save(character);
    }

    public void updateCharacter(String characterId, CharacterDto characterDto) {
        CharacterEntity character = characterRepository.findById(characterId).orElse(null);

        if (character == null) {
            throw new IllegalArgumentException("Character not found");
        }
        character.setEdition(characterDto.getEdition());
        character.setTags(characterDto.getTags());
        character.setDisabledBlocks(disabledBlocksMapper.toEntity(characterDto.getDisabledBlocks()));
        character.setSpells(spellsMapper.toEntity(characterDto.getSpells()));
        character.setData(characterDto.getData());

        characterRepository.save(character);
    }

    public Optional<CharacterDto> getCharacterById(String characterId) {
        return characterRepository.findById(characterId)
                .map(characterMapper::toDto);
    }

    public boolean deleteCharacter(String characterId) {
        if (characterRepository.existsById(characterId)) {
            characterRepository.deleteById(characterId);
            return true;
        }
        return false;
    }

    public List<CharacterDto> getAllCharactersByUserId(String userId) {
        List<CharacterEntity> entities = characterRepository.findByUserId(userId);
        return entities.stream()
                .map(characterMapper::toDto)
                .toList();
    }
}
