package org.fablewhirl.user.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.event.UserRemovedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return config;
    }

    @Bean
    public ProducerFactory<String, UserRegisteredEvent> userRegisteredEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }


    @Bean
    public KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate(
            ProducerFactory<String, UserRegisteredEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, UserRemovedEvent> userRemovedEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, UserRemovedEvent> userRemovedEventKafkaTemplate(
            ProducerFactory<String, UserRemovedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
