package com.example.portfolio_simple_spring_mvc.domain.port.repository.reader;

import java.util.List;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public interface AvatarReader {
    List<Avatar> selecAvatars_NotActive();
}
