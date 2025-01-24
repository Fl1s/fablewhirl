package org.fablewhirl.character.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.character.dto.CharacterDto;
import org.fablewhirl.character.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<CharacterDto> createCharacter(@PathVariable String userId) {
        return ResponseEntity.ok(characterService.createCharacter(userId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CharacterDto>> getAllCharacters(@PathVariable String userId) {
        List<CharacterDto> characters = characterService.getAllCharactersByUserId(userId);
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable String characterId) {
        return characterService.getCharacterById(characterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{characterId}")
    public ResponseEntity<CharacterDto> updateCharacter(@PathVariable String characterId, @RequestBody CharacterDto updatedDto) {
        return ResponseEntity.ok(characterService.updateCharacter(characterId, updatedDto));
    }

    @DeleteMapping("/{characterId}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String characterId) {
        if (characterService.deleteCharacter(characterId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

