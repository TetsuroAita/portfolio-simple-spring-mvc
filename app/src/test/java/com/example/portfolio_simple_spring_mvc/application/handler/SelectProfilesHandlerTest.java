package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfilesCommand;
import com.example.portfolio_simple_spring_mvc.application.command.ProfilesCommand.OrderBy;
import com.example.portfolio_simple_spring_mvc.application.dto.pagination.PaginationDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ProfileColumn;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPageRequest;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPagedResult;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;

@ExtendWith(MockitoExtension.class)
public class SelectProfilesHandlerTest {
    @Mock private ProfileReader profileReader;
    @Mock private HandledResultFactory handledResultFactory;
    @Mock private DomainPageRequest domainPageRequest;
    @Mock private DomainPagedResult<Profile> domainPagedResult;
    @Mock private ProfilesCommand.OrderBy command;
    @InjectMocks private SelectProfilesHandler handler;

    @Test
    @DisplayName("正常に取得")
    void test_ListIsNotEmpty() {
        List<Profile> profiles = List.of(Profile.createObjectForTest());
        ProfilesCommand.OrderBy command =
            new OrderBy(0, 10, ProfileColumn.PERSONAL_NUMBER, true, true);
        DomainPageRequest request =
            new DomainPageRequest(command.page(), command.size(), command.profileColumn().getColumnName(), command.asc());
        DomainPagedResult<Profile> domainPagedResult =
            new DomainPagedResult<>(profiles, 0, 1, 1, false, false);

        doReturn(domainPagedResult).when(profileReader).selectProfiles(command.activity(), request);
        
        handler.handle(command);

        verify(profileReader).selectProfiles(command.activity(), request);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.LIST_OF_PROFILE_SELECTED),
            argThat((PaginationDto<ResponseDto> paginationDto) ->
                paginationDto != null &&
                paginationDto.hasContent() &&
                paginationDto.currentPage() == 0 &&
                paginationDto.totalPages() == 1 &&
                paginationDto.totalElements() == 1 &&
                !paginationDto.hasNext() &&
                !paginationDto.hasPrevious()
            )
        );
    }

    @Test
    @DisplayName("取得したものが空")
    void test_ListIsEmpty() {
        List<Profile> profiles = List.of();
        ProfilesCommand.OrderBy command =
            new OrderBy(0, 10, ProfileColumn.PERSONAL_NUMBER, true, true);
        DomainPageRequest request =
            new DomainPageRequest(command.page(), command.size(), command.profileColumn().getColumnName(), command.asc());
        DomainPagedResult<Profile> domainPagedResult =
            new DomainPagedResult<>(profiles, 0, 0, 0, false, false);

        doReturn(domainPagedResult).when(profileReader).selectProfiles(command.activity(), request);
        
        handler.handle(command);

        verify(profileReader).selectProfiles(command.activity(), request);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.LIST_OF_PROFILE_SELECTED),
            argThat((PaginationDto<ResponseDto> paginationDto) ->
                paginationDto != null &&
                !paginationDto.hasContent() &&
                paginationDto.currentPage() == 0 &&
                paginationDto.totalPages() == 0 &&
                paginationDto.totalElements() == 0 &&
                !paginationDto.hasNext() &&
                !paginationDto.hasPrevious()
            )
        );
    }

    @Test
    @DisplayName("指定のページ番号にデータがなければ一つ前の番号を返す")
    void test_selectProfiles_withIncorrectPageNumber_returnCorrectNumber() {
        ProfilesCommand.OrderBy command =
            new OrderBy(1, 10, ProfileColumn.PERSONAL_NUMBER, true, true);
        DomainPageRequest request1 =
            new DomainPageRequest(command.page(), command.size(), command.profileColumn().getColumnName(), command.asc());
        DomainPageRequest request2 =
            new DomainPageRequest(command.page() - 1, command.size(), command.profileColumn().getColumnName(), command.asc());
        DomainPagedResult<Profile> domainPagedResultEmpty =
            new DomainPagedResult<>(List.of(), 1, 1, 1, false, false);
        DomainPagedResult<Profile> domainPagedResult =
            new DomainPagedResult<>(List.of(Profile.createObjectForTest()), 0, 1, 1, false, false);

        when(profileReader.selectProfiles(command.activity(), request1)).thenReturn(domainPagedResultEmpty);
        when(profileReader.selectProfiles(command.activity(), request2)).thenReturn(domainPagedResult);
        
        handler.handle(command);

        verify(profileReader).selectProfiles(command.activity(), request1);
        verify(profileReader).selectProfiles(command.activity(), request2);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.LIST_OF_PROFILE_SELECTED),
            argThat((PaginationDto<ResponseDto> paginationDto) ->
                paginationDto != null &&
                paginationDto.hasContent() &&
                paginationDto.currentPage() == 0 &&
                paginationDto.totalPages() == 1 &&
                paginationDto.totalElements() == 1 &&
                !paginationDto.hasNext() &&
                !paginationDto.hasPrevious()
            )
        );
    }
}