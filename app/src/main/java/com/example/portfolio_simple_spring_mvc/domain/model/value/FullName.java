package com.example.portfolio_simple_spring_mvc.domain.model.value;

import java.util.Objects;

import com.example.portfolio_simple_spring_mvc.domain.validation.FullNamePolicy;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/*
 * バリューオブジェクト(不変)
 * Hibernate 6.2+ なら recordクラスにすれば
 * フィールドは暗黙的に final
 * equals/hashCode/toStringも自動生成
 */
@Embeddable
public class FullName {

    @Column(name = "last_name", nullable = false, length = 10)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 10)
    private String firstName;

    @Column(name = "last_name_kana", nullable = false, length = 10)
    private String lastNameKana;

    @Column(name = "first_name_kana", nullable = false, length = 10)
    private String firstNameKana;

    //リフレクション用
    @Deprecated
    protected FullName() {}

    //新規作成用
    public FullName(String lastName, String firstName, String lastNameKana, String firstNameKana) {
        checkArguments(lastName, firstName, lastNameKana, firstNameKana);
        this.lastName = lastName;
        this.firstName = firstName;
        this.lastNameKana = lastNameKana;
        this.firstNameKana = firstNameKana;
    }
    
    //ゲッター
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getLastNameKana() { return lastNameKana; }
    public String getFirstNameKana() { return firstNameKana; }
    public String getFullName() { return lastName + " " + firstName; }
    public String getFullNameKana() { return lastNameKana + " " + firstNameKana; }

    //更新時は常にnewするのでsetterはなし

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof FullName)) return false;
        FullName other = (FullName)obj;
        return Objects.equals(lastName, other.lastName)
            && Objects.equals(firstName, other.firstName)
            && Objects.equals(lastNameKana, other.lastNameKana)
            && Objects.equals(firstNameKana, other.firstNameKana);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, lastNameKana, firstNameKana);
    }
    
    //インスタンス初期化前にバリデーションチェック
    private static void checkArguments(String lastName, String firstName, String lastNameKana, String firstNameKana) {
        FullNamePolicy.notBlank(lastName, firstName, lastNameKana, firstNameKana);
        FullNamePolicy.size10(lastName, firstName, lastNameKana, firstNameKana);
        FullNamePolicy.only_Japanese(lastName, firstName);
        FullNamePolicy.only_Katakana(lastNameKana, firstNameKana);
    }

    public static FullName createObjectForTest() {
        return new FullName(
            "山田", 
            "太郎", 
            "ヤマダ", 
            "タロウ"
        );
    }
}
