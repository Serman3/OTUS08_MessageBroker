package ru.stoloto.homework.messageBroker;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.stoloto.homework.messageBroker.command.Command;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;
import ru.stoloto.homework.messageBroker.model.GameContext;
import ru.stoloto.homework.messageBroker.model.GameRegistry;
import ru.stoloto.homework.messageBroker.service.KafkaProducerService;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@Testcontainers
class MessageBrokerApplicationTests {

    // Объявляем KafkaContainer как @Container — Testcontainers сам поднимет и убьёт его
    @Container
    static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3")
    );
    @Autowired
    private KafkaProducerService<String, EndpointEventMessageDto> endpointEventProducerService;
    @Autowired
    private Environment environment;
    @Autowired
    private CountDownLatch countDownLatch;
    @Autowired
    private GameRegistry gameRegistry;
    private GameContext gameContext;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @BeforeEach
    public void init() {
        gameContext = gameRegistry.getGame("TEST_GAME");
    }

    @Test
    public void readMessageTest() throws InterruptedException {

        EndpointEventMessageDto endpointEventMessageDto = new EndpointEventMessageDto(
                "TEST_GAME"
                ,"GAME_OBJECT_1"
                ,"SET_VELOCITY"
                ,Map.of("velocity","33")
        );

        endpointEventProducerService.sendMessage(new ProducerRecord<>(environment.getProperty("kafka.topic"), "endpoint_event_message", endpointEventMessageDto));

        countDownLatch.await();

        gameContext.getCommandQueue().forEach(Command::execute);
        Assertions.assertEquals(33, gameContext.getObject("GAME_OBJECT_1").getProperty("velocity"));
    }

}
