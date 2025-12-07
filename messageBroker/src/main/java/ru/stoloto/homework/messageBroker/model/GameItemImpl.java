package ru.stoloto.homework.messageBroker.model;

import org.springframework.stereotype.Component;
import ru.stoloto.homework.messageBroker.annotation.Id;

import java.util.HashMap;
import java.util.Map;

@Id("GAME_OBJECT_1")
@Component
public class GameItemImpl implements GameItem {

    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public Object getProperty(String key) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        throw new RuntimeException("Свойство " + key + " не найдено");
    }

    @Override
    public String getId() {
        return this.getClass().getAnnotation(Id.class).value();
    }

    @Override
    public void setProperty(String key, Object value) {
        if (key != null && value != null) {
            properties.put(key, value);
        } else {
            throw new RuntimeException("Ключ и значение не могут быть null");
        }
    }

}
