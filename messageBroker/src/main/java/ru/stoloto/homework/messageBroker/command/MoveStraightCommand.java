package ru.stoloto.homework.messageBroker.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.stoloto.homework.messageBroker.annotation.Id;
import ru.stoloto.homework.messageBroker.model.GameItem;

import java.util.Map;

@Id("MOVE")
@Component
@Scope("prototype")
public class MoveStraightCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveStraightCommand.class);

    private final GameItem gameObject;
    private final Map<String, Object> args;

    public MoveStraightCommand(GameItem gameObject, Map<String, Object> args) {
        this.gameObject = gameObject;
        this.args = args;
    }

    @Override
    public void execute() {
        Integer point = Integer.parseInt((String) args.get("point"));
        gameObject.setProperty("point", point);
        LOGGER.info("Moving object {} with point {}", gameObject.getId(), point);
    }
}
