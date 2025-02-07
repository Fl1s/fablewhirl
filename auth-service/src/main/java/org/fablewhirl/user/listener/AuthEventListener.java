package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthEventListener {

    private final KafkaTemplate<String, UserRegistrationEvent> userRegistrationTemplate;
    private final AuthService authService;

    @KafkaListener(topics = "user-registered", groupId = "auth-service")
    public boolean handleUserRegistration(UserRegistrationEvent event) {
        try {
            authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());
            userRegistrationTemplate.send("user-registration", event);
            return ResponseEntity.ok("[User successfully registered!]").hasBody();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("[User registration failed...]").hasBody();
        }
    }

    @KafkaListener(topics = "user-login", groupId = "auth-service")
    public ResponseEntity<?> handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
