package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthEventListener {

    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;
    private final AuthService authService;

    @KafkaListener(topics = "user-registered", groupId = "auth-service")
    public void handleUserRegistration(UserRegisteredEvent event) {
        try {
            authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());
            userRegisteredTemplate.send("user-registration", event);
            System.out.println("User successfully registered in Keycloak.");
        } catch (Exception e) {
            System.err.println("Error registering user in Keycloak: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "user-login", groupId = "auth-service")
    public void handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            System.out.println("User login successful: " + tokenResponse);
        } catch (Exception e) {
            System.err.println("Invalid username or password: " + e.getMessage());
        }
    }
}
