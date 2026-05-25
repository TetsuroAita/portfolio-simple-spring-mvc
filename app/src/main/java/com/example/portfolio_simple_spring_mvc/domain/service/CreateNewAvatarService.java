package com.example.portfolio_simple_spring_mvc.domain.service;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.domain.executor.Executor;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;

public class CreateNewAvatarService implements Executor {
    private final AvatarWriter avatarWriter;
    private final AvatarStorageClient avatarStorageClient;

    public CreateNewAvatarService(
        AvatarWriter avatarWriter,
        AvatarStorageClient avatarStorageClient
    ) {
        this.avatarWriter = avatarWriter;
        this.avatarStorageClient = avatarStorageClient;
    }

    public Avatar createOf(FileSource fileSource) {
        Avatar newAvatar = avatarWriter.insertAvatar(
            new Avatar(
                fileSource.getInfo().originalFilename(),
                fileSource.getInfo().contentType(),
                fileSource.getInfo().contentSize()
            )
        );

        avatarStorageClient.uploadAvatar(
            fileSource,
            newAvatar.generatePath()
        );

        return newAvatar;
    }
}
