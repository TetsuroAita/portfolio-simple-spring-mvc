package com.example.portfolio_simple_spring_mvc.application.dto.filesource;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.multipart.MultipartFile;

record SimpleFileSource(

    String filename,
    String contentType,
    long contentSize,
    IOFunction<InputStream> streamSupplier

) implements FileSource {

    public static FileSource of(MultipartFile file) {
        nullCheck(file);
        return new SimpleFileSource(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            file :: getInputStream
        );
    }

    public static FileSource of(ConvertibleClientHttpResponse response) {
        nullCheck(response);
        return new SimpleFileSource(
            response.getHeaders().getContentDisposition().getFilename(),
            response.getHeaders().getContentType().toString(),
            response.getHeaders().getContentLength(),
            response :: getBody
        );
    }

    @FunctionalInterface
    public interface IOFunction<T> {
        T apply() throws IOException;
    }

    @Override
    public FileSourceInfo getInfo() {
        FileSourceInfo info = new FileSourceInfo(filename, contentType, contentSize);
        return info;
    }

    @Override
    public InputStream openStream() throws IOException {
        return streamSupplier.apply();
    }

    private static void nullCheck(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                "value is null."
            );
        }
    }
}
