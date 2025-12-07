package ru.stoloto.homework.messageBroker.broker.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;


import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EndpointEventDeserializer implements Deserializer<EndpointEventMessageDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointEventDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public EndpointEventMessageDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                LOGGER.warn("Null received at deserializing");
                return null;
            }
            LOGGER.info("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), EndpointEventMessageDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to EndpointEventMessageDto");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
