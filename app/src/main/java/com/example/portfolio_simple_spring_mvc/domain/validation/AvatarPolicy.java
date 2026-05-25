package com.example.portfolio_simple_spring_mvc.domain.validation;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public interface AvatarPolicy {
    void validate(FileSource fileSource, Avatar avatar);
}
