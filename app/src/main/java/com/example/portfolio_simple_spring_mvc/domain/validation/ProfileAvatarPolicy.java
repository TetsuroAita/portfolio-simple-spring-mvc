package com.example.portfolio_simple_spring_mvc.domain.validation;

import java.util.Objects;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public class ProfileAvatarPolicy implements AvatarPolicy {

    private final long MAX_SIZE = 1L * 1024 * 1024;

    // アップロードするファイルの条件を満たしているか確認
    @Override
    public void validate(FileSource fileSource, Avatar avatar) {
        String filename = fileSource.getInfo().originalFilename();
        String contentType = fileSource.getInfo().contentType();
        long size = fileSource.getInfo().contentSize();

        // コンテントタイプのサプタイプが許可されたものかどうか
        boolean allowedImageType = AllowedImageType.isAllowed(contentType);
        
        // 同名のファイルはアップロード不可
        if (avatar != null) {
            String originalFilename = avatar.getOriginalFilename();

            if (Objects.equals(filename, originalFilename)) {
                throw new DomainValidationException(DomainErrorMessage.SAME_FILE_ALREADY_EXIST);
            }
        }

        if(size > MAX_SIZE) {
            throw new DomainValidationException(DomainErrorMessage.FILESIZE_OVER);
        }

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new DomainValidationException(DomainErrorMessage.PROFILE_AVATAR_INVALID);
        }
        
        if (allowedImageType == false) {
            throw new DomainValidationException(DomainErrorMessage.PROFILE_AVATAR_INVALID);
        }
    }
}
