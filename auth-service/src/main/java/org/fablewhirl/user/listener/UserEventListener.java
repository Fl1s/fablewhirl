package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;
    // private final KafkaTemplate<String, CheckUserExistenceEvent> userExistenceTemplate;
    //  private final KafkaTemplate<String, UserLoginEvent> userLoginTemplate;
    private final AuthService authService;

    @KafkaListener(topics = "user-registration", groupId = "auth-service")
    public ResponseEntity<?> handleUserRegistration(UserRegisteredEvent event) {
        try {
            authService.registerUser(event.getUsername(), event.getEmail(), event.getPassword());
            userRegisteredTemplate.send("user-registered", event);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered in Keycloak.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering user in Keycloak: " + e.getMessage());
        }
    }

    public ResponseEntity<?> handleUserLogin(UserLoginEvent event) {
        try {
            AccessTokenResponse tokenResponse = authService.authenticateUser(event.getUsername(), event.getPassword());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password: " + e.getMessage());
        }
    }
}
