package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.*;
import org.fablewhirl.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthListener {
    private static final Logger logger = Logger.getLogger(AuthListener.class.getName());
    private final AuthService authService;

    @KafkaListener(topics = "user-registered", groupId = "auth-service",
            containerFactory = "kafkaListenerContainerFactoryUserRegistered")
    public void finalizeRegistration(UserRegisteredEvent event) {
        if (event.isUserExists()) {
            logger.info("[User successfully registered with ID: " + event.getUserId() + "]");
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
