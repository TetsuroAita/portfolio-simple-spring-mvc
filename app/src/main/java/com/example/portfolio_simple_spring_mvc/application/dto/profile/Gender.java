package com.example.portfolio_simple_spring_mvc.application.dto.profile;

public enum Gender {
    
    MALE("男性"),
    FEMALE("女性"),
    OTHER("その他");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }

    public static Gender convert(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException();
        }

        for (Gender gender : values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }

        throw new IllegalArgumentException();
    }
}
