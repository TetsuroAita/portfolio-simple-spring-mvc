package com.example.portfolio_simple_spring_mvc.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.dispatcher.CommandHandlerDispatcher;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.StorageUnavailableException;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.SupabaseStorageException;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.config.SecurityConfig;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;
import com.example.portfolio_simple_spring_mvc.presentation.Presenter;

@WebMvcTest(ProfileAvatarController.class)
@Import(SecurityConfig.class)
public class ProfileAvatarControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CommandHandlerDispatcher dispatcher;
    @MockitoBean private Presenter presenter;
    @MockitoBean private MessageUtil messageUtil;

    private static UUID PROFILE_ID = UUID.randomUUID();
    private static MockMultipartFile FILE =
        new MockMultipartFile(
            "file",
            "test.png",
            "image/png",
            "content".getBytes()
        );

    @Test
    @DisplayName("アバター取得成功")
    void test_selectProfileAvatar_statusIsOk() throws Exception {
        when(presenter.present(any(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isOk());

        verify(dispatcher).dispatch(
            argThat(command ->
                command instanceof ProfileAvatarCommand.Select &&
                ((ProfileAvatarCommand.Select) command).base().profileId().equals(PROFILE_ID)
            )
        );
    }

    @Test
    @DisplayName("アバター取得時 ProfileNotFoundException で GlobalExceptionHandler でキャッチ400で返す")
    void test_catchProfileNotFoundException_whenSelectProfileAvatar() throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(get("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isBadRequest())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "400: Bad Request"))
            .andExpect(model().attribute("message", "エラー"));
    }

    @ParameterizedTest
    @MethodSource("provideException")
    @DisplayName("アバター取得時 ProfileNotFoundException 以外の例外は GlobalExceptionHandler でキャッチして500で返す")
    void test_catchException_whenSelectProfileAvatar(RuntimeException exception) throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(exception);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(get("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isInternalServerError())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "500: Internal Server Error"))
            .andExpect(model().attribute("message", "エラー"));
    }
    
    @Test
    @DisplayName("アップロード成功")
    void test_changeProfileAvatar_statusIsOk() throws Exception {
        when(presenter.present(any(), any())).thenReturn(ResponseEntity.ok().build());
        
        mockMvc.perform(multipart("/profile-avatar/" + PROFILE_ID).file(FILE))
        .andExpect(status().isOk());
        
        verify(dispatcher).dispatch(
            argThat(command ->
                command instanceof ProfileAvatarCommand.Change &&
                ((ProfileAvatarCommand.Change) command).base().profileId().equals(PROFILE_ID)
            )
        );
    }
    
    @Test
    @DisplayName("アップロード時 DomaniValidationException は ProfileAvatarExceptionHandler でキャッチして400で返す")
    void test_catchDomainValidationException_whenChangeProfileAvatar() throws Exception {
        when(dispatcher.dispatch(any()))
        .thenThrow(DomainValidationException.class);
        when(messageUtil.getMessage(any()))
        .thenReturn("エラー");
        
        mockMvc.perform(multipart("/profile-avatar/" + PROFILE_ID).file(FILE))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Content-Type","application/problem+json"))
        .andExpect(jsonPath("$.title").value("422: Unprocessable Entity"))
        .andExpect(jsonPath("$.detail").value("エラー"))
        .andExpect(jsonPath("$.instance").value("/profile-avatar/" + PROFILE_ID));
    }

    @Test
    @DisplayName("アップロード時 ProfileNotFoundException で GlobalExceptionHandler でキャッチして400で返す")
    void test_catchProfileNotFoundException_whenUpdateProfileAvatar() throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(multipart("/profile-avatar/" + PROFILE_ID).file(FILE))
            .andExpect(status().isBadRequest())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "400: Bad Request"))
            .andExpect(model().attribute("message", "エラー"));
    }
    
    @ParameterizedTest
    @MethodSource("provideException")
    @DisplayName("アップロード時 ProfileNotFoundException、 DomaniValidationException 以外は GlobalExceptionHandler でキャッチして500で返す")
    void test_catchException_whenChangeProfileAvatar(RuntimeException exception) throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(exception);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(multipart("/profile-avatar/" + PROFILE_ID).file(FILE))
            .andExpect(status().isInternalServerError())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "500: Internal Server Error"))
            .andExpect(model().attribute("message", "エラー"));
    }

    @Test
    @DisplayName("アバター削除成功")
    void test_deleteProfileAvatar_statusIsOk() throws Exception {
        when(presenter.present(any(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isOk());

        verify(dispatcher).dispatch(
            argThat(command ->
                command instanceof ProfileAvatarCommand.Delete &&
                ((ProfileAvatarCommand.Delete) command).base().profileId().equals(PROFILE_ID)
            )
        );
    }

    @Test
    @DisplayName("アバター削除時に ProfileNotFoundException で GlobalExceptionHandler でキャッチして400で返す")
    void test_catchProfileNotFoundException_whenDeleteProfileAvatar() throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(delete("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isBadRequest())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "400: Bad Request"))
            .andExpect(model().attribute("message", "エラー"));
    }

    @ParameterizedTest
    @MethodSource("provideException")
    @DisplayName("アバター削除時 ProfileNotFoundException 以外の例外は GlobalExceptionHandler でキャッチして500で返す")
    void test_catchException_whenDeleteProfileAvatar(RuntimeException exception) throws Exception {
        when(dispatcher.dispatch(any()))
            .thenThrow(exception);
        when(messageUtil.getMessage(any()))
            .thenReturn("エラー");

        mockMvc.perform(delete("/profile-avatar/" + PROFILE_ID))
            .andExpect(status().isInternalServerError())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("title", "500: Internal Server Error"))
            .andExpect(model().attribute("message", "エラー"));
    }

    private static Stream<Arguments> provideException() {
        return Stream.of(
            Arguments.of(new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST)),
            Arguments.of(new SupabaseStorageException(null)),
            Arguments.of(new StorageUnavailableException()),
            Arguments.of(new RuntimeException())
        );
    }
}
