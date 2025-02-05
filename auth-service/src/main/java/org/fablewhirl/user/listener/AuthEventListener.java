package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthEventListener {

    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;
    private final AuthService authService;

    private static final Logger logger = Logger.getLogger(AuthEventListener.class.getName());

    @KafkaListener(topics = "user-registration", groupId = "auth-service")
    public ResponseEntity<?> handleUserRegistration(UserRegisteredEvent event) {
        logger.info("Processing user registration for username: " + event.getUsername());
        try {
            authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());
            userRegisteredTemplate.send("user-registered", event);
            logger.info("User successfully registered in Keycloak: " + event.getUsername());
        } catch (Exception e) {
            logger.severe("Error registering user in Keycloak: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @KafkaListener(topics = "user-login", groupId = "auth-service")
    public ResponseEntity<?> handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            logger.info("User login successful: " + event.getUsername() + " | Token: " + tokenResponse.getToken());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            logger.severe("Invalid username or password: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}
