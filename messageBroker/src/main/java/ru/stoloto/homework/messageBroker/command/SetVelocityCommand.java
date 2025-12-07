package ru.stoloto.homework.messageBroker.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.stoloto.homework.messageBroker.annotation.Id;
import ru.stoloto.homework.messageBroker.model.GameItem;

import java.util.Map;

@Id("SET_VELOCITY")
@Component
@Scope("prototype")
public class SetVelocityCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetVelocityCommand.class);

    private final GameItem gameObject;
    private final Map<String, Object> args;

    public SetVelocityCommand(GameItem gameObject, Map<String, Object> args) {
        this.gameObject = gameObject;
        this.args = args;
    }

    @Override
    public void execute() {
        int velocity = Integer.parseInt((String) args.get("velocity"));
        gameObject.setProperty("velocity", velocity);
        LOGGER.info("Set velocity {} for object {}", velocity, gameObject.getId());
    }
}
