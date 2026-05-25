package com.example.portfolio_simple_spring_mvc.application.dto.filesource;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.multipart.MultipartFile;

class GenericFileSource implements FileSource {
    
    private final String filename;
    private final String contentType;
    private final long contentSize;
    private final StreamSupplier streamSupplier;

    @FunctionalInterface
    private interface StreamSupplier {
        InputStream get() throws IOException;
    }

    // MultipartFile 用コンストラクタ
    public GenericFileSource(MultipartFile file) {
        this.filename = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.contentSize = file.getSize();
        this.streamSupplier = file :: getInputStream;
    }

    // ストレージから取得したデータの変換用コンストラクタ
    public GenericFileSource(ConvertibleClientHttpResponse response) {
        this.filename = response.getHeaders().getContentDisposition().getFilename();
        this.contentType = response.getHeaders().getContentType().toString();
        this.contentSize = response.getHeaders().getContentLength();
        this.streamSupplier = response :: getBody;
    }

    @Override
    public FileSourceInfo getInfo() {
        FileSourceInfo info = new FileSourceInfo(filename, contentType, contentSize);
        return info;
    }

    @Override
    public InputStream openStream() throws IOException {
        return this.streamSupplier.get();
    }
}
