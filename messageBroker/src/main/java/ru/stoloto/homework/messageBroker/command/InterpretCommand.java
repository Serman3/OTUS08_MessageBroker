package ru.stoloto.homework.messageBroker.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stoloto.homework.messageBroker.model.GameContext;
import ru.stoloto.homework.messageBroker.model.GameItem;

import java.util.Map;

public class InterpretCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterpretCommand.class);

    private final String operationId;
    private final String objectId;
    private final Map<String, Object> args;
    private final CommandFactory commandFactory;
    private final GameContext gameContext;

    public InterpretCommand(
            String operationId,
            String objectId,
            Map<String, Object> args,
            CommandFactory commandFactory,
            GameContext gameContext) {
        this.operationId = operationId;
        this.objectId = objectId;
        this.args = args;
        this.commandFactory = commandFactory;
        this.gameContext = gameContext;
    }

    @Override
    public void execute() {
        try {
            GameItem gameObject = gameContext.getObject(objectId);
            if (gameObject == null) {
                throw new IllegalArgumentException("Game object not found: " + objectId);
            }

            Command command = commandFactory.createCommand(operationId, gameObject, args);

            gameContext.addCommand(command);
        } catch (Exception e) {
            LOGGER.error("Error in InterpretCommand execution: {}", e.getMessage(), e);
        }
    }
}
