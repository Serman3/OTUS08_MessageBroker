package ru.stoloto.homework.messageBroker.command;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.stoloto.homework.messageBroker.annotation.Id;
import ru.stoloto.homework.messageBroker.model.GameItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CommandFactory {

    private static final String SCAN_PACKAGE = "ru.stoloto.homework.messageBroker.command";
    private final ApplicationContext applicationContext;
    private final Map<String, Class<? extends Command>> commandRegistry;

    @Autowired
    public CommandFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.commandRegistry = new HashMap<>();
        initCommand();
    }

    public Command createCommand(String operationId, GameItem gameObject, Map<String, Object> args) {
        Class<? extends Command> commandClass = commandRegistry.get(operationId);
        if (commandClass == null) {
            throw new IllegalArgumentException("Unknown operation: " + operationId);
        }

        return applicationContext.getBean(commandClass, gameObject, args);
    }

    public void addCommand(String id, Class<? extends Command> cmdClass) {
        commandRegistry.put(id, cmdClass);
    }

    private void initCommand() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(SCAN_PACKAGE)
                .addScanners(Scanners.TypesAnnotated));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Id.class);
        classes.stream()
                .filter(Command.class::isAssignableFrom)
                .filter(clazz -> !clazz.equals(Command.class))
                .map(clazz -> (Class<? extends Command>) clazz)
                .forEach(cmd -> {
                    Id id = cmd.getAnnotation(Id.class);
                    addCommand(id.value(), cmd);
                });
    }
}
