package ru.stoloto.homework.messageBroker.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameRegistry {

    private final Map<String, GameContext> games = new ConcurrentHashMap<>();

    @Autowired
    public GameRegistry(List<GameContext> gameContextList) {
        gameContextList.forEach(this::registerGame);
    }

    public GameContext getGame(String gameId) {
        return games.get(gameId);
    }

    public void registerGame(GameContext gameContext) {
        games.put(gameContext.getId(), gameContext);
    }
}
