package com.example.portfolio_simple_spring_mvc.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;

public class FullNameTest {
    
    @Test
    @DisplayName("正常系：正しい値ですべてのフィールドが正しく設定される")
    void createFullName_success() {
        //given
        FullName fullName = new FullName("山田", "太郎", "ヤマダ", "タロウ");

        //when, then
        assertThat(fullName.getLastName()).isEqualTo("山田");
        assertThat(fullName.getFirstName()).isEqualTo("太郎");
        assertThat(fullName.getLastNameKana()).isEqualTo("ヤマダ");
        assertThat(fullName.getFirstNameKana()).isEqualTo("タロウ");
        assertThat(fullName.getFullName()).isEqualTo("山田 太郎");
        assertThat(fullName.getFullNameKana()).isEqualTo("ヤマダ タロウ");
    }

    @Test
    @DisplayName("異常系：nullを渡すとDomainValidationExceptionをスローする")
    void createFullName_null_throwsException() {
        assertThatThrownBy(() -> new FullName(null, "太郎", "ヤマダ", "タロウ"))
            .isInstanceOf(DomainValidationException.class)
            .hasMessage(DomainErrorMessage.NOT_BLANK.getKey());
    }

    @Test
    @DisplayName("異常系：空文字を渡すとDomainValidationExceptionをスローする")
    void createFullName_blank_throwsException() {
        assertThatThrownBy(() -> new FullName("", "太郎", "ヤマダ", "タロウ"))
            .isInstanceOf(DomainValidationException.class)
            .hasMessage(DomainErrorMessage.NOT_BLANK.getKey());
    }

    @Test
    @DisplayName("異常系：名前が10文字を超えるとDomainValidationExceptionをスローする")
    void createFullName_tooLong_throwsException() {
        String longName = "あ".repeat(11);
        assertThatThrownBy(() -> new FullName(longName, "太郎", "ヤマダ", "タロウ"))
            .isInstanceOf(DomainValidationException.class)
            .hasMessage(DomainErrorMessage.SIZE10.getKey());
    }

    @Test
    @DisplayName("異常系：漢字・ひらがな以外を含む場合はDomainValidationExceptionをスローする")
    void createFullName_invalidCharacters_throwsException() {
        assertThatThrownBy(() -> new FullName("Yamada", "太郎", "ヤマダ", "タロウ"))
            .isInstanceOf(DomainValidationException.class)
            .hasMessage(DomainErrorMessage.ONLY_JAPANESE.getKey());
    }

    @Test
    @DisplayName("異常系：カナにひらがなを含むとDomainValidationExceptionをスローする")
    void createFullName_invalidKana_throwsException() {
        assertThatThrownBy(() -> new FullName("山田", "太郎", "やまだ", "たろう"))
            .isInstanceOf(DomainValidationException.class)
            .hasMessage(DomainErrorMessage.ONLY_KATAKANA.getKey());
    }

    @Test
    @DisplayName("equalsとhashCodeの一致確認")
    void equalsAndHashCode() {
        FullName name1 = new FullName("山田", "太郎", "ヤマダ", "タロウ");
        FullName name2 = new FullName("山田", "太郎", "ヤマダ", "タロウ");
        FullName name3 = new FullName("佐藤", "花子", "サトウ", "ハナコ");

        assertThat(name1).isEqualTo(name2);
        assertThat(name1).hasSameHashCodeAs(name2);
        assertThat(name1).isNotEqualTo(name3);
    }
}
