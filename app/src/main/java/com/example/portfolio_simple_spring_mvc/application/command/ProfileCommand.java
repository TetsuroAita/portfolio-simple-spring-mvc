package com.example.portfolio_simple_spring_mvc.application.command;

import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.handler.CommandHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.DeleteProfileHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.InsertProfileHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.SelectProfileForEditHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.SelectProfileHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.UnDeletedProfileHandler;
import com.example.portfolio_simple_spring_mvc.application.handler.UpdateProfileHandler;

public interface ProfileCommand extends Command {

    // ===== 静的ファクトリメソッド =====
    static Select select(UUID profileId) {
        return new Select(
            profileId
        );
    }

    static Select_ForEdit select_ForEdit(UUID profileId) {
        return new Select_ForEdit(
            profileId
        );
    }

    static Insert insert(RequestDto requestDto) {
        return new Insert(
            requestDto
        );
    }

    static Update update(UUID profileId, RequestDto requestDto) {
        return new Update(
            profileId,
            requestDto
        );
    }

    static Delete delete(UUID profileId) {
        return new Delete(
            profileId
        );
    }

    static UnDeleted unDeleted(UUID profileId) {
        return new UnDeleted(
            profileId
        );
    }

    // ===== 定義 =====
    record Select(
        UUID profileId
    ) implements ProfileCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return SelectProfileHandler.class;
        }
    }

    record Select_ForEdit(
        UUID profileId
    ) implements ProfileAvatarCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return SelectProfileForEditHandler.class;
        }
    }

    record Insert(
        RequestDto requestDto
    ) implements ProfileCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return InsertProfileHandler.class;
        }
    }

    record Update(
        UUID profileId,
        RequestDto requestDto
    ) implements ProfileCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return UpdateProfileHandler.class;
        }
    }

    record Delete(
        UUID profileId
    ) implements ProfileCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return DeleteProfileHandler.class;
        }
    }

    record UnDeleted(
        UUID profileId
    ) implements ProfileCommand {
        @Override
        public Class<? extends CommandHandler<?, ?>> getHandlerType() {
            return UnDeletedProfileHandler.class;
        }
    }
}
