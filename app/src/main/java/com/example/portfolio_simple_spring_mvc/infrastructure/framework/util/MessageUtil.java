package com.example.portfolio_simple_spring_mvc.infrastructure.framework.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private MessageSource messageSource;

    public MessageUtil(
        MessageSource messageSource
    ) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageKey) {
        return getMessage(messageKey, new Object[0]);
    }

    public String getMessage(String messageKey, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, locale);
    }
}