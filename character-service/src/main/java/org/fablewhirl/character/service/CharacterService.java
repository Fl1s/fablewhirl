package org.fablewhirl.character.service;

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
    public CharacterDto createCharacter(String userId) {
        CharacterEntity character = new CharacterEntity();
        character.setUserId(userId);
        return characterMapper.toDto(characterRepository.save(character));
    }

    public List<CharacterDto> getAllCharacters() {
        return characterRepository.findAll().stream()
                .map(characterMapper::toDto)
                .toList();
    }

    public List<CharacterDto> getAllCharactersByUserId(String userId) {
        return characterRepository.findByUserId(userId).stream()
                .map(characterMapper::toDto)
                .toList();
    }

    public Optional<CharacterDto> getCharacterById(String characterId) {
        return characterRepository.findById(characterId)
                .map(characterMapper::toDto);
    }

    @Transactional
    public CharacterDto updateCharacter(String characterId, CharacterDto characterDto) {
        CharacterEntity character = characterRepository.findById(characterId).orElse(null);

        if (character == null) {
            throw new IllegalArgumentException("[Character not found!]");
        }
        character.setEdition(characterDto.getEdition());
        character.setTags(characterDto.getTags());
        character.setDisabledBlocks(disabledBlocksMapper.toEntity(characterDto.getDisabledBlocks()));
        character.setSpells(spellsMapper.toEntity(characterDto.getSpells()));
        character.setData(characterDto.getData());

        return characterMapper.toDto(characterRepository.save(character));
    }

    @Transactional
    public boolean deleteCharacter(String characterId) {
        if (characterRepository.existsById(characterId)) {
            characterRepository.deleteById(characterId);
            return true;
        }
        return false;
    }
}
