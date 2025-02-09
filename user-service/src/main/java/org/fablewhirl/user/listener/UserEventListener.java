package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.CheckUserExistenceEvent;
import org.fablewhirl.user.event.UserExistenceCheckedEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final UserService userService;
    private final KafkaTemplate<String, UserExistenceCheckedEvent> userCheckedTemplate;
    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;

    @KafkaListener(topics = "check-user-existence", groupId = "user-service",
            containerFactory = "kafkaListenerContainerFactoryCheckUserExistence"
    )
    public void handleCheckUserExistence(CheckUserExistenceEvent event) {
        boolean userExists = userService.existsByUsernameOrEmail(event.getUsername(), event.getEmail());
        userCheckedTemplate.send(
                "user-existence-checked",
                new UserExistenceCheckedEvent(event.getCorrelationId(), userExists)
        );
    }
    @KafkaListener(topics = "user-registration", groupId = "user-group",
    containerFactory = "kafkaListenerContainerFactoryUserRegistration")
    public void listenerUserRegistration(UserRegistrationEvent event) {
        userService.register(event);

        UserRegisteredEvent user =userService.getUserByUsername(event.getUsername());
        userRegisteredTemplate.send(
                "user-registered",
                new UserRegisteredEvent(
                        event.getCorrelationId(),
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        event.getPassword()
                )
        );
    }
}
