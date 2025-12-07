package ru.stoloto.homework.messageBroker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointEventMessageDto {

    private String gameId;
    private String gameObjectId;
    private String operationId;
    private Map<String, Object> args;

    public EndpointEventMessageDto() {}

    public EndpointEventMessageDto(String gameId, String gameObjectId, String operationId, Map<String, Object> args) {
        this.gameId = gameId;
        this.gameObjectId = gameObjectId;
        this.operationId = operationId;
        this.args = args;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameObjectId() {
        return gameObjectId;
    }

    public void setGameObjectId(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

}
