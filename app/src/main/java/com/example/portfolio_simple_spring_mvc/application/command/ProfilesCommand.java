package com.example.portfolio_simple_spring_mvc.application.command;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.ProfileColumn;
import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.SelectProfilesHandler;

public interface ProfilesCommand extends Command {
    
    record OrderBy(
        int page,
        int size,
        ProfileColumn profileColumn,
        boolean asc,
        boolean activity
    ) implements ProfilesCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return SelectProfilesHandler.class;
        }
    }
}
