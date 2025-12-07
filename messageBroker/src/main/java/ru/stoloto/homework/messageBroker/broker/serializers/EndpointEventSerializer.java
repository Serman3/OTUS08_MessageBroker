package ru.stoloto.homework.messageBroker.broker.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;

import java.util.Map;

public class EndpointEventSerializer implements Serializer<EndpointEventMessageDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointEventSerializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, EndpointEventMessageDto endpointEventMessageDto) {
        try {
            if (endpointEventMessageDto == null) {
                LOGGER.warn("Null received at serializing");
                return null;
            }
            LOGGER.info("Serializing...");
            return objectMapper.writeValueAsBytes(endpointEventMessageDto);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing EndpointEventMessageDto to byte[]");
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
