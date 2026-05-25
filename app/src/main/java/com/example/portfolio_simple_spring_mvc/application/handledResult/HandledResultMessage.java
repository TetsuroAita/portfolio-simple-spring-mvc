package com.example.portfolio_simple_spring_mvc.application.handledResult;

public enum HandledResultMessage {
    
    // Profile CRUD関連
    LIST_OF_PROFILE_SELECTED("list_of_profile_selected"),
    PROFILE_SELECTED("profile_selected"),
    PROFILE_SELECTED_FOR_EDIT("profile_selected_for_edit"),
    PROFILE_INSERTED("profile_inserted"),
    PROFILE_UPDATED("profile_updated"),
    PROFILE_DELETED("profile_deleted"),
    PROFILE_UN_DELETED("profile_un_deleted"),

    // Avatar CRUD関連
    AVATAR_SELECTED("avatar_selected"),
    AVATAR_INSERTED("avatar_inserted"),
    AVATAR_UPDATED("avatar_updated"),
    AVATAR_DELETED("avatar_deleted");
    
    private final String key;

    HandledResultMessage(
        String key
    ) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
