package com.example.portfolio_simple_spring_mvc.application.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;

@Configuration
public class DispatcherConfig {
    
    @SuppressWarnings("rawtypes")
    @Bean
    public CommandHandlerDispatcher commandHandlerDispatcher(
        ApplicationContext context
    ) {
        Map<String, CommandHandler> beans = context.getBeansOfType(CommandHandler.class);

        List<CommandHandler> handlers = new ArrayList<>(beans.values());

        return new CommandHandlerDispatcher(handlers);
    }
}