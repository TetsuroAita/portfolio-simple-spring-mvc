package com.example.portfolio_simple_spring_mvc.domain.exception;

public enum DomainErrorMessage {

    // 不正なリクエスト
    BAD_REQUEST("bad_request"),
    PROFILE_NOT_FOUND("profile_not_found"),

    // バリデーション
    NOT_BLANK("not_blank"),
    ONLY_JAPANESE("only_japanese"),
    ONLY_KATAKANA("only_katakana"),
    SIZE10("size10"),
    SIZE200("size200"),
    PROFILE_AVATAR_INVALID("profile_avatar_invalid"),
    SAME_FILE_ALREADY_EXIST("same_file_already_exist"),
    FILESIZE_OVER("filesize_over");
    
    private String key;

    DomainErrorMessage(
        String key
    ) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}