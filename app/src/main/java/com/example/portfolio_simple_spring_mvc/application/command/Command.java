package com.example.portfolio_simple_spring_mvc.application.command;

import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;

public interface Command {
    Class<? extends CommandHandler<?, ?>> getHandlerType();
}