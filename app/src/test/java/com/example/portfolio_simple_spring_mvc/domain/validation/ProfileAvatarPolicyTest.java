package com.example.portfolio_simple_spring_mvc.domain.validation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSourceInfo;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public class ProfileAvatarPolicyTest {
    private ProfileAvatarPolicy profileAvatarPolicy;
    private Avatar avatar;
    private FileSource fileSource;
    private FileSourceInfo info;
    private String filename;
    private String contentType;
    private long contentSize;
    private String originalFilename;

    @BeforeEach
    void setup() {
        profileAvatarPolicy = new ProfileAvatarPolicy();
        avatar = mock(Avatar.class);
        fileSource = mock(FileSource.class);
        filename = "test.png";
        contentType = "image/png";
        contentSize = 1L;
        originalFilename = "original.png";
    }

    @Test
    @DisplayName("ファイル名が既存のファイル名と同じであれば例外")
    void valid_filenameIsSame_throwDomainException() {
        info = new FileSourceInfo(filename, contentType, contentSize);
        originalFilename = "test.png";
        when(fileSource.getInfo()).thenReturn(info);
        when(avatar.getOriginalFilename()).thenReturn(originalFilename);

        assertThatThrownBy(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    @DisplayName("ファイル名が既存のファイル名と異なればあれば成功")
    void valid_filenameIsDifferent_void() {
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        when(avatar.getOriginalFilename()).thenReturn(originalFilename);

        assertThatCode(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ファイルサイズが範囲内で成功")
    public void valid_contenSize_isEven_void() {
        contentSize = 1L * 1024 * 1024;
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        
        assertThatCode(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ファイルサイズがオーバーで例外")
    public void valid_contentSize_isOver_throwDomainException() {
        contentSize = 1L * 1024 * 1024 + 1;
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        
        assertThatThrownBy(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    @DisplayName("contentType が許容されたものなら成功")
    public void valid_contentType_isAllowed_throwDomainException() {
        contentType = "image/png";
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        assertThatCode(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .doesNotThrowAnyException();

        contentType = "image/jpeg";
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        assertThatCode(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("contentType が許容されたものでないなら例外")
    public void valid_contentType_isNotAllowed_throwDomainException() {
        contentType = null;
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        assertThatThrownBy(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .isInstanceOf(DomainValidationException.class);

        contentType = "text/plain";
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        assertThatThrownBy(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .isInstanceOf(DomainValidationException.class);

        contentType = "image/gif";
        info = new FileSourceInfo(filename, contentType, contentSize);
        when(fileSource.getInfo()).thenReturn(info);
        assertThatThrownBy(() -> profileAvatarPolicy.validate(fileSource, avatar))
            .isInstanceOf(DomainValidationException.class);
    }
}
