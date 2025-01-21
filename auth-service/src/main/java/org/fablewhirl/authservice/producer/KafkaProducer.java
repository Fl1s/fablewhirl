package org.fablewhirl.authservice.producer;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.authservice.event.RegistrationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, RegistrationEvent> kafkaTemplate;

    public void sendRegistration(RegistrationEvent event) {
        kafkaTemplate.send("user-registration", event);
    }
}


