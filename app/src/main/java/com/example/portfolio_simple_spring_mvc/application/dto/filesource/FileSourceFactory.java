package com.example.portfolio_simple_spring_mvc.application.dto.filesource;

import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.multipart.MultipartFile;

public final class FileSourceFactory {

    private FileSourceFactory() {}
    
    public static FileSource of(MultipartFile file) {
        nullCheck(file);
        return new GenericFileSource(file);
    }

    public static FileSource of(ConvertibleClientHttpResponse response) {
        nullCheck(response);
        return new GenericFileSource(response);
    }

    private static void nullCheck(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                "value is null."
            );
        }
    }
}
