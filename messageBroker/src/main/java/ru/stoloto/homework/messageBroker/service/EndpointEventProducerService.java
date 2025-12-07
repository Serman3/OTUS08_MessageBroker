package ru.stoloto.homework.messageBroker.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;

import java.util.Optional;

@Service
public class EndpointEventProducerService extends BaseKafkaProducer<String, EndpointEventMessageDto> implements KafkaProducerService<String, EndpointEventMessageDto> {

    @Autowired
    public EndpointEventProducerService(KafkaTemplate<String, EndpointEventMessageDto> endpointKafkaTemplate) {
        super(endpointKafkaTemplate);
    }

    @Override
    public Optional<RecordMetadata> sendMessage(ProducerRecord<String, EndpointEventMessageDto> producerRecord) {
        return send(producerRecord);
    }

}
