package ru.stoloto.homework.messageBroker.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.stoloto.homework.messageBroker.command.CommandFactory;
import ru.stoloto.homework.messageBroker.command.InterpretCommand;
import ru.stoloto.homework.messageBroker.dto.EndpointEventMessageDto;
import ru.stoloto.homework.messageBroker.model.GameContext;
import ru.stoloto.homework.messageBroker.model.GameRegistry;

import java.util.concurrent.CountDownLatch;

@Service
public class EndpointEventConsumerService implements KafkaConsumerService<String, EndpointEventMessageDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointEventConsumerService.class);

    private final GameRegistry gameRegistry;
    private final CommandFactory commandFactory;
    private final CountDownLatch countDownLatch;

    @Autowired
    public EndpointEventConsumerService(
            GameRegistry gameRegistry,
            CommandFactory commandFactory,
            CountDownLatch countDownLatch) {
        this.gameRegistry = gameRegistry;
        this.commandFactory = commandFactory;
        this.countDownLatch = countDownLatch;
    }

    @Override
    @KafkaListener(topics = "${kafka.topic}")
    public void processMessage(ConsumerRecord<String, EndpointEventMessageDto> consumerRecord) {
        EndpointEventMessageDto message = consumerRecord.value();
        LOGGER.info("Received message for game: {}, object: {}, operation: {}", message.getGameId(), message.getGameObjectId(), message.getOperationId());

        try {
            GameContext gameContext = gameRegistry.getGame(message.getGameId());
            if (gameContext == null) {
                LOGGER.error("Game not found with id: {}", message.getGameId());
                return;
            }

            InterpretCommand interpretCommand = new InterpretCommand(
                    message.getOperationId(),
                    message.getGameObjectId(),
                    message.getArgs(),
                    commandFactory,
                    gameContext
            );

            interpretCommand.execute();
            countDownLatch.countDown();
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
        }

    }

}
