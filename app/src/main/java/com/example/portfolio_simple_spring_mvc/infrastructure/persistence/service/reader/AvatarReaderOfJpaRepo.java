package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.reader;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.AvatarReader;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.AvatarRepository;

@Component("avatarReaderOfJpaRepo")
public class AvatarReaderOfJpaRepo implements AvatarReader {
    private final AvatarRepository avatarRepository;

    public AvatarReaderOfJpaRepo(
        AvatarRepository avatarRepository
    ) {
        this.avatarRepository = avatarRepository;
    }

    @Override
    public List<Avatar> selecAvatars_NotActive() {
        return avatarRepository.findByActiveFalse();
    }
}
