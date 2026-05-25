package com.example.portfolio_simple_spring_mvc.domain.executor;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;

public class DeleteProfileExecutor implements Executor {
    private final ProfileWriter profileWriter;

    public DeleteProfileExecutor(
        ProfileWriter profileWriter
    ) {
        this.profileWriter = profileWriter;
    }

    public ProfileDeletedEvent execute(DeleteProfilePlan plan) {
        if (plan.profile_Inactivate() && plan.profile_Delete()) {
            throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
        }

        if (!plan.profile_Inactivate() && !plan.profile_Delete()) {
            throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
        }

        if (plan.profile_Inactivate()) {
            profileWriter.updateProfile(plan.profile().inactivate());
            return null;
        }

        if (plan.profile_Delete()) {
            profileWriter.deleteProfile(plan.profile());
            return new ProfileDeletedEvent(plan.profile());
        }

        throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
    }
}
