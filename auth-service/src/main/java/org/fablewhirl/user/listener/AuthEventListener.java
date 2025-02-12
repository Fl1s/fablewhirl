package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.*;
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

    private final KafkaTemplate<String, UserRegistrationEvent> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveEvent> userRemoveTemplate;
    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthEventListener.class.getName());

    public void handleUserRegistration(UserRegistrationEvent event) {
        try {
            String keycloakUserId = authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());

            UserRegistrationEvent registrationEvent = new UserRegistrationEvent(
                    event.getCorrelationId(),
                    keycloakUserId,
                    event.getEmail(),
                    event.getUsername(),
                    event.getPassword(),
                    event.getBio()
            );

            userRegistrationTemplate.send("user-registration", registrationEvent);

            logger.info("[User successfully registered! Keycloak ID: " + keycloakUserId + "]");
        } catch (Exception e) {
            logger.severe("[User registration failed. Error: " + e.getMessage() + "]");
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service",
            containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void finalizeRegistration(UserRegisteredEvent event) {
        if (event.isUserExists()) {
            logger.info("[User successfully registered with ID: " + event.getUserId() + "]");
        }
    }

    public ResponseEntity<?> handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            logger.info("[User login successful! Access token generated.]");
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            logger.severe("[User login failed. Error: " + e.getMessage() + "]");
            ResponseEntity.status(401).build();
        }
        return ResponseEntity.status(401).build();
    }

    public void handleUserRemove(UserRemoveEvent event) {
        try {
            userRemoveTemplate.send("user-remove", event);
            logger.info("[User logged out successfully!]");
        } catch (Exception e) {
            logger.severe("[Logout failed. Error: " + e.getMessage() + "]");
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service",
            containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public ResponseEntity<?> finalizeRemove(UserRemovedEvent event) {
        try {
            if (!event.isUserExists()) {
                authService.removeUser(event.getUserId());
                logger.info("[User with ID: " + event.getUserId() + " successfully removed.]");
            }
            return ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            logger.severe("[Logout failed. Error: " + e.getMessage() + "]");
            return ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }
}
