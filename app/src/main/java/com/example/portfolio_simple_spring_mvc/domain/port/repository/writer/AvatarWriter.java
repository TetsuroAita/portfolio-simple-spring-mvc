package com.example.portfolio_simple_spring_mvc.domain.port.repository.writer;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;

public interface AvatarWriter {
    Avatar insertAvatar(Avatar avatar);
    Avatar updateAvatar(Avatar avatar);
    long deleteAvatars();
}
