package ru.stoloto.homework.messageBroker.broker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import ru.stoloto.homework.messageBroker.broker.serializers.EndpointEventDeserializer;
import ru.stoloto.homework.messageBroker.broker.serializers.EndpointEventSerializer;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class KafkaTopicEndpointEventConfig {

    private final Environment environment;

    @Autowired
    public KafkaTopicEndpointEventConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ConsumerFactory<String, String> consumerEndpointFactory() {
        Map<String, Object> consumerConfigProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Arrays.stream(environment.getProperty("spring.kafka.bootstrap-servers").split(",")).toList(),
                ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EndpointEventDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("spring.kafka.consumer.auto-offset-reset")
        );
        return new DefaultKafkaConsumerFactory<>(consumerConfigProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerEndpointFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, EndpointEventMessageDto> producerEndpointFactory() {
        Map<String, Object> producerConfigProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Arrays.stream(environment.getProperty("spring.kafka.bootstrap-servers").split(",")).toList(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EndpointEventSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(producerConfigProps);
    }

    @Bean
    public KafkaTemplate<String, EndpointEventMessageDto> endpointKafkaTemplate() {
        return new KafkaTemplate<>(producerEndpointFactory());
    }

    @Bean
    public NewTopic endpointEventTopic() {
        return TopicBuilder.name(environment.getProperty("kafka.topic"))
                .partitions(3)
                .replicas(2)
                .build();
    }

}
