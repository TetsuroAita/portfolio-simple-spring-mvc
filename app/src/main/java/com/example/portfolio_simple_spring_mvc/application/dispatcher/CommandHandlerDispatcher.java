package com.example.portfolio_simple_spring_mvc.application.dispatcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.portfolio_simple_spring_mvc.application.command.Command;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;

public class CommandHandlerDispatcher {
    private final Map<Class<?>, CommandHandler<?, ?>> handlerMap = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    public CommandHandlerDispatcher(List<CommandHandler> handlers) {
        for (CommandHandler handler : handlers) {
            handlerMap.put(handler.getHandlerType(), handler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T, C extends Command> HandledResult<T> dispatch(C command) {
        CommandHandler<T, C> handler = (CommandHandler<T, C>) handlerMap.get(command.getHandlerType());

        if (handler == null) {
            throw new RuntimeException("Handler not found: " + command.getHandlerType());
        }

        return handler.handle(command);
    }
}
