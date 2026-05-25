package com.example.portfolio_simple_spring_mvc.application.command;

import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.ChangeProfileAvatarHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.DeleteProfileAvatarHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.SelectProfileAvatarHandler;


public interface ProfileAvatarCommand extends Command {

    // ===== 静的ファクトリメソッド =====
    static Select select(UUID profileId) {
        return new Select(
            new Base(profileId)
        );
    }

    static Change change(UUID profileId, FileSource fileSource) {
        return new Change(
            new Base(profileId),
            fileSource
        );
    }

    static Delete delete(UUID profileId) {
        return new Delete(
            new Base(profileId)  
        );
    }

    // ===== 定義 =====
    record Base(UUID profileId) {}

    record Select(
        Base base
    ) implements ProfileAvatarCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return SelectProfileAvatarHandler.class;
        }
    }
    
    record Change(
        Base base,
        FileSource fileSource
    ) implements ProfileAvatarCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return ChangeProfileAvatarHandler.class;
        }
    }

    record Delete(
        Base base
    ) implements ProfileAvatarCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return DeleteProfileAvatarHandler.class;
        }
    }
}
