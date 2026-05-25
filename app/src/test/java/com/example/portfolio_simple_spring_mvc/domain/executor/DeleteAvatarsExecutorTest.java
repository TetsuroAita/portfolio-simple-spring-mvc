package com.example.portfolio_simple_spring_mvc.domain.executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.AvatarReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;

@ExtendWith(MockitoExtension.class)
public class DeleteAvatarsExecutorTest {
    @Mock private AvatarReader avatarReader;
    @Mock private AvatarWriter avatarWriter;
    @Mock private AvatarStorageClient avatarStorageClient;
    @Mock private Avatar avatar;

    @InjectMocks private DeleteAvatarsExecutor executor;

    @Test
    void test_anyAvatarsHavaToBeDelete_returncCount() {
        List<Avatar> avatars = List.of(avatar);
        when(avatarReader.selecAvatars_NotActive()).thenReturn(avatars);
        when(avatarWriter.deleteAvatars()).thenReturn(1L);

        long result = executor.execute();

        assertThat(result).isEqualTo(1L);
        verify(avatarStorageClient).deleteAvatars(any());
    }
    
    @Test
    void test_NoAvatarsHavaToBeDelete_returncCountIsZero() {
        List<Avatar> avatars = List.of();
        when(avatarReader.selecAvatars_NotActive()).thenReturn(avatars);

        long result = executor.execute();

        assertThat(result).isEqualTo(0);
        verifyNoInteractions(avatarWriter, avatarStorageClient);
    }

    @Test
    void test_throwRuntimeException_atAvatarStorageClient_throwRuntimeException() {
        List<Avatar> avatars = List.of(avatar);
        when(avatarReader.selecAvatars_NotActive()).thenReturn(avatars);
        doThrow(RuntimeException.class).when(avatarStorageClient).deleteAvatars(any());

        assertThatThrownBy(() -> executor.execute())
            .isInstanceOf(RuntimeException.class);
        verify(avatarReader).selecAvatars_NotActive();
        verify(avatarStorageClient).deleteAvatars(any());
        verify(avatarWriter).deleteAvatars();
    }
}
