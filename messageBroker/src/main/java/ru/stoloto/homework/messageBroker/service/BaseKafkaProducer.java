package ru.stoloto.homework.messageBroker.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

public abstract class BaseKafkaProducer<K,V> {

    private final static Logger log = LoggerFactory.getLogger(BaseKafkaProducer.class);

    private final KafkaTemplate<K, V> kafkaTemplate;

    protected BaseKafkaProducer(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    protected Optional<RecordMetadata> send(ProducerRecord<K,V> producerRecord) {
        Optional<RecordMetadata> result = Optional.empty();
        try {
            result = Optional.of(kafkaTemplate.send(producerRecord).get().getRecordMetadata());
        } catch (Exception e) {
            log.error("""
                    Не удалось отправить сообщение в топик: {}
                    С ключом: {}
                    Значение: {}
                    """, producerRecord.topic(), producerRecord.key(), producerRecord.value());
        }
        return result;
    }

}
