package com.example.portfolio_simple_spring_mvc.domain.port.storage;

import java.util.List;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;

public interface AvatarStorageClient {
    String selectAvatar(String path);
    void uploadAvatar(FileSource fileSource, String path);
    void deleteAvatars(List<String> prefixes);
}
