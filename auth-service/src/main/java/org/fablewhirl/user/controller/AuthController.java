package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisteredEvent event) {
        kafkaTemplate.send("user-registration", event);
        return ResponseEntity.ok("User registered successfully");
    }
}
