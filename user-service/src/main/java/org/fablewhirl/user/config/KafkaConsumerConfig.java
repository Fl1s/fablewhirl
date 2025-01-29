package org.fablewhirl.user.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.event.CheckUserExistenceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, UserRegisteredEvent> userRegisteredEventConsumerFactory() {
        JsonDeserializer<UserRegisteredEvent> deserializer = new JsonDeserializer<>(UserRegisteredEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        ErrorHandlingDeserializer<UserRegisteredEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorHandlingDeserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConsumerFactory<String, CheckUserExistenceEvent> checkUserExistenceEventConsumerFactory() {
        JsonDeserializer<CheckUserExistenceEvent> deserializer = new JsonDeserializer<>(CheckUserExistenceEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        ErrorHandlingDeserializer<CheckUserExistenceEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorHandlingDeserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserRegisteredEvent>> kafkaListenerContainerFactoryUserRegistered() {
        ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userRegisteredEventConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CheckUserExistenceEvent>> kafkaListenerContainerFactoryCheckUserExistence() {
        ConcurrentKafkaListenerContainerFactory<String, CheckUserExistenceEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(checkUserExistenceEventConsumerFactory());
        return factory;
    }
}
