package org.fablewhirl.user.listener;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.RegistrationEvent;
import org.fablewhirl.user.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListener {
    private final UserService userService;
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "user")
    public void consume(RegistrationEvent userData) {
        userService.registerUser(userData);
    }
}
