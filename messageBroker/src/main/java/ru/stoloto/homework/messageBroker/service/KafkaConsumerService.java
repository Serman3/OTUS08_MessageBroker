package ru.stoloto.homework.messageBroker.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumerService<K,V> {

    void processMessage(ConsumerRecord<K, V> consumerRecord);
}
