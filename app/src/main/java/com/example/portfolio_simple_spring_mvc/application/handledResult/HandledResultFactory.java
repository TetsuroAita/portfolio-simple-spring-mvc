package com.example.portfolio_simple_spring_mvc.application.handledResult;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

@Service
public class HandledResultFactory {
    private final MessageUtil messageUtil;

    public HandledResultFactory(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    public <T> HandledResult<T> of(
        HandledResultMessage handledResultMessage
    ) {
        return of(handledResultMessage, null);
    }

    public <T> HandledResult<T> of(
        HandledResultMessage handledResultMessage,
        T data
    ) {
        Assert.notNull(handledResultMessage, "HandledResultMessage is null.");
        String message = messageUtil.getMessage(handledResultMessage.getKey());
        return new HandledResult<>(message, data);
    }
}
