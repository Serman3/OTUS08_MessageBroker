package ru.stoloto.homework.messageBroker.model;

public interface GameItem {
    String getId();
    void setProperty(String key, Object value);
    Object getProperty(String key);
}
