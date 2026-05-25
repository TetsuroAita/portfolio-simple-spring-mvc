package com.example.portfolio_simple_spring_mvc.application.dto.profile;

public enum ProfileColumn {
    
    // エンティティのフィールド名(カラム名ではない)
    PERSONAL_NUMBER("personalNumber", "ID"),
    LAST_NAME_KANA("fullName_lastNameKana", "名前"), // fullNmameで経由でアクセス
    GENDER("gender", "性別"),
    DATE_OF_BIRTH("dateOfBirth", "年齢");

    private final String columnName;
    private final String label;

    ProfileColumn(String columnName, String label) {
        this.columnName = columnName;
        this.label = label;
    }

    @Override
    public String toString() {
        return name();
    }    

    public String getColumnName() {
        return this.columnName;
    }

    public String getLabel() {
        return this.label;
    }
}
