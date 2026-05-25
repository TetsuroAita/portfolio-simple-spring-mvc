package com.example.portfolio_simple_spring_mvc.domain.validation;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;

public final class FullNamePolicy {
    
    private FullNamePolicy() {}

    public static void notBlank(String... values) {
        for(String value : values) {
            if(value == null || value.isBlank()) {
                throw new DomainValidationException(DomainErrorMessage.NOT_BLANK);
            }
        }
    }

    public static void only_Japanese(String... values) {
        for(String value : values) {
            if(!value.matches("^[\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}ー]+$")) {
                throw new DomainValidationException(DomainErrorMessage.ONLY_JAPANESE);
            } 
        }
    }

    public static void only_Katakana(String... values) {
        for(String value : values) {
            if(!value.matches("^[\\u30A0-\\u30FFー]+$")) {
                throw new DomainValidationException(DomainErrorMessage.ONLY_KATAKANA);
            }
        }
    }

    public static void size10(String... values) {
        for(String value : values) {
            if(value.length() > 10) {
                throw new DomainValidationException(DomainErrorMessage.SIZE10);
            }
        }
    }
    public static void size200(String... values) {
        for(String value : values) {
            if(value.length() > 200) {
                throw new DomainValidationException(DomainErrorMessage.SIZE200);
            }
        }
    }
}
