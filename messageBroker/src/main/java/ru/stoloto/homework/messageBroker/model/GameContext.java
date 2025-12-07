package ru.stoloto.homework.messageBroker.model;

import ru.stoloto.homework.messageBroker.command.Command;

import java.util.concurrent.BlockingQueue;

public interface GameContext {

    String getId();

    BlockingQueue<Command> getCommandQueue();

    GameItem getObject(String objectId);

    void addObject(GameItem object);

    void addCommand(Command command);

}
