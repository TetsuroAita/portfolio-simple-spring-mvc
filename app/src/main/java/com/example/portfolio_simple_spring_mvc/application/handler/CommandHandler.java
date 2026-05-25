package com.example.portfolio_simple_spring_mvc.application.handler;

import com.example.portfolio_simple_spring_mvc.application.command.Command;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;

public interface CommandHandler<T, C extends Command> {
    HandledResult<T> handle(C command);
    Class<? extends CommandHandler<?, ?>> getHandlerType();
}
