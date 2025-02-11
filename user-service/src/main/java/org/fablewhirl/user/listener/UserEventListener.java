package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.*;
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
    private final KafkaTemplate<String, UserRemovedEvent> userRemovedTemplate;

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

        userRegisteredTemplate.send(
                "user-registered",
                new UserRegisteredEvent(
                        event.getCorrelationId(),
                        event.getUserId(),
                        userService.existsById(event.getUserId())
                )
        );
    }

    @KafkaListener(topics = "user-remove", groupId = "user-group",
            containerFactory = "kafkaListenerContainerFactoryUserRemove")
    public void listenerUserRemove(UserRemoveEvent event) {
        userService.deleteUser(event.getUserId());

        userRemovedTemplate.send(
                "user-removed",
                new UserRemovedEvent(
                        event.getCorrelationId(),
                        event.getUserId(),
                        userService.existsById(event.getUserId())
                )
        );
    }
}
