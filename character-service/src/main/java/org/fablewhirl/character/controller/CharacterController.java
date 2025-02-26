package org.fablewhirl.character.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.character.dto.CharacterDto;
import org.fablewhirl.character.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @Transactional
    @PostMapping
    public ResponseEntity<CharacterDto> createCharacter(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(201)
                .body(characterService.createCharacter(jwt.getSubject()));
    }

    @GetMapping
    public ResponseEntity<List<CharacterDto>> getAllCharacters() {
        List<CharacterDto> characters = characterService.getAllCharacters();
        return characters.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(characters);
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<CharacterDto>> getAllCharactersByUserId(@AuthenticationPrincipal Jwt jwt) {
        List<CharacterDto> characters = characterService.getAllCharactersByUserId(jwt.getSubject());
        return characters.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(characters);
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable String characterId) {
        return characterService.getCharacterById(characterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PatchMapping("/{characterId}")
    public ResponseEntity<CharacterDto> updateCharacter(@PathVariable String characterId, @RequestBody CharacterDto updatedDto) {
        return ResponseEntity.ok(characterService.updateCharacter(characterId, updatedDto));
    }

    @Transactional
    @DeleteMapping("/{characterId}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String characterId) {
        if (characterService.deleteCharacter(characterId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


