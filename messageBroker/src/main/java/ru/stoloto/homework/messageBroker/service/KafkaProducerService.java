package ru.stoloto.homework.messageBroker.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Optional;

public interface KafkaProducerService<K, V> {

    Optional<RecordMetadata> sendMessage(ProducerRecord<K, V> producerRecord);

}
