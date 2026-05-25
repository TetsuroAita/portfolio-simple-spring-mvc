package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfilesCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.pagination.PaginationDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.application.util.ProfileMapper;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPageRequest;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPagedResult;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;

@Service
public class SelectProfilesHandler implements CommandHandler<PaginationDto<ResponseDto>, ProfilesCommand.OrderBy> {
    private final ProfileReader profileReader;
    private final HandledResultFactory handledResultFactory;

    public SelectProfilesHandler(
        @Qualifier("profileReaderOfJpaRepo") ProfileReader profileReader,
        HandledResultFactory handledResultFactory
    ) {
        this.profileReader = profileReader;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public HandledResult<PaginationDto<ResponseDto>> handle(ProfilesCommand.OrderBy command) {
        DomainPageRequest pageRequest = new DomainPageRequest(
            command.page(),
            command.size(),
            command.profileColumn().getColumnName(),
            command.asc()
        );

        DomainPagedResult<Profile> result = profileReader.selectProfiles(command.activity(), pageRequest);

        if (!result.hasContent() && result.currentPage() > 0) {
            pageRequest = new DomainPageRequest(
                command.page() - 1,
                command.size(),
                command.profileColumn().getColumnName(),
                command.asc()
            );

            result = profileReader.selectProfiles(command.activity(), pageRequest);
        }

        PaginationDto<ResponseDto> responseDtos = new PaginationDto<>(
            result.content().stream().map(ProfileMapper :: toResponseDto).toList(),
            result.currentPage(),
            result.totalPages(),
            result.totalElements(),
            result.hasNext(),
            result.hasPrevious()
        );

        return handledResultFactory.of(HandledResultMessage.LIST_OF_PROFILE_SELECTED, responseDtos);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return SelectProfilesHandler.class;
    }
}
