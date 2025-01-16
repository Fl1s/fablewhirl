package org.fablewhirl.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "character-service", url = "http://localhost:8082")
public interface CharacterClient {
    @GetMapping("/characters/{characterId}")
    Character getCharacterById(@PathVariable("characterId") Long characterId);
}
