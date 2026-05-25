package com.example.portfolio_simple_spring_mvc.domain.executor;

import java.util.List;

import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.AvatarReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;

public class DeleteAvatarsExecutor implements Executor {
    private final AvatarReader avatarReader;
    private final AvatarWriter avatarWriter;
    private final AvatarStorageClient avatarStorageClient;

    public DeleteAvatarsExecutor(
        AvatarReader avatarReader,
        AvatarWriter avatarWriter,
        AvatarStorageClient avatarStorageClient
    ) {
        this.avatarReader = avatarReader;
        this.avatarWriter = avatarWriter;
        this.avatarStorageClient = avatarStorageClient;
    }

    public long execute() {
        List<String> avatars =
            avatarReader.selecAvatars_NotActive()
            .stream()
            .map(avatar -> avatar.generatePath())
            .toList();

        if (!avatars.isEmpty()) {
            long count = avatarWriter.deleteAvatars();
            avatarStorageClient.deleteAvatars(avatars);
            return count;
        }

        return 0;
    }
}
