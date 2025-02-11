package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.*;
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
    private final KafkaTemplate<String, UserRemoveEvent> userRemoveTemplate;
    private final AuthService authService;

    public boolean handleUserRegistration(UserRegistrationEvent event) {
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

            System.out.println("[User successfully registered! Keycloak ID: " + keycloakUserId + "]");
            return true;
        } catch (Exception e) {
            System.err.println("[User registration failed...]");
            e.printStackTrace();
            return false;
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "auth-service",
            containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void finalizeRegistration(UserRegisteredEvent event) {
        try {
            if (event.isUserExists()) {
                System.out.println("[User successfully registered with ID: " + event.getUserId() + "]");
            }
        } catch (Exception e) {
            System.err.println("[Failed to register user in Keycloak]");
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    public void handleUserRemove(UserRemoveEvent event) {
        try {
            userRemoveTemplate.send("user-remove", event);
            ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }

    @KafkaListener(topics = "user-removed", groupId = "auth-service",
            containerFactory = "kafkaListenerContainerFactoryUserRemoved")
    public ResponseEntity<?> finalizeRemove(UserRemovedEvent event) {
        try {
            if (!event.isUserExists()) {
                authService.removeUser(event.getUserId());
            }
            return ResponseEntity.ok("[User logged out successfully!]");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Logout failed: " + e.getMessage() + "]");
        }
    }
}
