package org.fablewhirl.user.producer;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.event.UserRemoveEvent;
import org.fablewhirl.user.listener.AuthListener;
import org.fablewhirl.user.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthProducer {
    private static final Logger logger = Logger.getLogger(AuthListener.class.getName());
    private final KafkaTemplate<String, UserRegistrationEvent> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveEvent> userRemoveTemplate;
    private final AuthService authService;

    public void handleUserRegistration(UserRegistrationEvent event) {
        try {
            String keycloakUserId = authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());

            userRegistrationTemplate.send("user-registration", new UserRegistrationEvent(
                    event.getCorrelationId(),
                    keycloakUserId,
                    event.getEmail(),
                    event.getUsername(),
                    event.getPassword(),
                    event.getBio()
            ));

            logger.info("[User successfully registered! Keycloak ID: " + keycloakUserId + "]");
        } catch (Exception e) {
            logger.severe("[User registration failed. Error: " + e.getMessage() + "]");
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
}
