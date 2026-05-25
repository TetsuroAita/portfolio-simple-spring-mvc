package com.example.portfolio_simple_spring_mvc.application.dto.filesource;

import java.io.IOException;
import java.io.InputStream;

public interface FileSource {
    FileSourceInfo getInfo();
    InputStream openStream() throws IOException;
}
