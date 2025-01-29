package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.CheckUserExistenceEvent;
import org.fablewhirl.user.event.UserExistenceCheckedEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> correlationMap = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;
    private final KafkaTemplate<String, CheckUserExistenceEvent> userExistenceTemplate;

    @KafkaListener(topics = "user-existence-checked", groupId = "auth-service")
    public void handleUserExistenceChecked(UserExistenceCheckedEvent event) {
        CompletableFuture<Boolean> future = correlationMap.remove(event.getCorrelationId());
        if (future != null) {
            future.complete(event.isUserExists());
        }
    }

    public ResponseEntity<?> handleUserRegistration(UserRegisteredEvent event) {
        String correlationId = UUID.randomUUID().toString();

        CompletableFuture<Boolean> userExistsFuture = new CompletableFuture<>();
        correlationMap.put(correlationId, userExistsFuture);

        userExistenceTemplate.send("check-user-existence",
                new CheckUserExistenceEvent(correlationId, event.getUsername(), event.getEmail()));

        try {
            boolean userExists = userExistsFuture.get(5, TimeUnit.SECONDS);
            if (userExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(format(
                        "[User with same username or email already exists. Registration failed.]",
                        event.getUsername(), event.getEmail()));
            }

        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body("[Request timed out while verifying user existence.]");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("[An unexpected error occurred during user verification.]");
        }
        userRegisteredTemplate.send("user-registration", event);

        return ResponseEntity.status(HttpStatus.CREATED).body(format(
                "[User has been successfully registered with email '%s'.]",
                event.getUsername(), event.getEmail()));
    }
}
