package ru.stoloto.homework.messageBroker.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stoloto.homework.messageBroker.annotation.Id;
import ru.stoloto.homework.messageBroker.command.Command;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class GameTestContext implements GameContext {

    private final String id;
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
    private final Map<String, GameItem> gameObjects = new ConcurrentHashMap<>();

    @Autowired
    public GameTestContext(List<GameItem> gameItemList) {
        this.id = "TEST_GAME";
        gameItemList.stream().filter(gi -> gi.getClass().isAnnotationPresent(Id.class)).forEach(gi -> {
            Id id = gi.getClass().getAnnotation(Id.class);
            addGameItem(id.value(), gi);
        });
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BlockingQueue<Command> getCommandQueue() {
        return commandQueue;
    }

    @Override
    public GameItem getObject(String objectId) {
        return gameObjects.get(objectId);
    }

    @Override
    public void addObject(GameItem object) {
        gameObjects.put(object.getId(), object);
    }

    @Override
    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    public void addGameItem(String id, GameItem gameItem) {
        gameObjects.put(id, gameItem);
    }
}
