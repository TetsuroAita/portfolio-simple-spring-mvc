package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.writer;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.AvatarRepository;

@Component("avatarWriterOfJpaRepo")
public class AvatarWriterOfJpaRepo implements AvatarWriter {
    private final AvatarRepository avatarRepository;

    public AvatarWriterOfJpaRepo(
        AvatarRepository avatarRepository
    ) {
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Avatar insertAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    @Override
    public Avatar updateAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    @Override
    public long deleteAvatars() {
        return avatarRepository.deleteByActiveFalse();
    }
}
