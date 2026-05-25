package com.example.portfolio_simple_spring_mvc.domain.executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;

@ExtendWith(MockitoExtension.class)
public class DeleteProfileExecutorTest {
    @Mock private ProfileWriter profileWriter;
    @InjectMocks private DeleteProfileExecutor executor;

    @Mock private Profile mockProfile;

    @Test
    void test_returnNull() {
        when(mockProfile.inactivate()).thenReturn(mockProfile);
        DeleteProfilePlan plan = new DeleteProfilePlan(
            mockProfile,
            true,
            false
        );
        ProfileDeletedEvent expect = null;

        ProfileDeletedEvent result = executor.execute(plan);

        assertThat(result).isEqualTo(expect);
        verify(profileWriter).updateProfile(mockProfile);
    }

    @Test
    void test_returnProfileDeletedEvent() {
        DeleteProfilePlan plan = new DeleteProfilePlan(
            mockProfile,
            false,
            true
        );
        ProfileDeletedEvent expect = new ProfileDeletedEvent(mockProfile);

        ProfileDeletedEvent result = executor.execute(plan);

        assertThat(result).isEqualTo(expect);
        verify(profileWriter).deleteProfile(mockProfile);
    }

    @Test
    void test_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> executor.execute(
            new DeleteProfilePlan(mockProfile, true, true)
        ))
        .isInstanceOf(DomainIllegalStateException.class);

        assertThatThrownBy(() -> executor.execute(
            new DeleteProfilePlan(mockProfile, false, false)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }
}
