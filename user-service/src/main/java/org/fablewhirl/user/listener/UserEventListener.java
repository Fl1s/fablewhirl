package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.event.UserRemoveEvent;
import org.fablewhirl.user.event.UserRemovedEvent;
import org.fablewhirl.user.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final UserService userService;
    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;
    private final KafkaTemplate<String, UserRemovedEvent> userRemovedTemplate;

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
