package org.fablewhirl.user.producer;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserRegistrationDto;
import org.fablewhirl.user.dto.UserRemoveDto;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.event.UserRemoveEvent;
import org.fablewhirl.user.listener.AuthListener;
import org.fablewhirl.user.service.AuthService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthProducer {
    private static final Logger logger = Logger.getLogger(AuthListener.class.getName());
    private final KafkaTemplate<String, UserRegistrationEvent> userRegistrationTemplate;
    private final KafkaTemplate<String, UserRemoveEvent> userRemoveTemplate;
    private final AuthService authService;

    public void handleUserRegistration(UserRegistrationDto dto) {
        try {
            String keycloakUserId = authService.registerUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
            String correlationId = UUID.randomUUID().toString();
            userRegistrationTemplate.send("user-registration", new UserRegistrationEvent(
                    correlationId,
                    keycloakUserId,
                    dto.getEmail(),
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getBio()
            ));

            logger.info("[User successfully registered! Keycloak ID: " + keycloakUserId + "]");
        } catch (Exception e) {
            logger.severe("[User registration failed. Error: " + e.getMessage() + "]");
        }
    }

    public void handleUserRemove(UserRemoveDto dto) {
        String correlationId = UUID.randomUUID().toString();
        try {
            userRemoveTemplate.send("user-remove", new UserRemoveEvent(
                    correlationId,
                    dto.getUserId()
            ));
            logger.info("[User logged out successfully!]");
        } catch (Exception e) {
            logger.severe("[Logout failed. Error: " + e.getMessage() + "]");
        }
    }
}
