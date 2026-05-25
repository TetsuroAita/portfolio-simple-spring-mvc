package com.example.portfolio_simple_spring_mvc.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.portfolio_simple_spring_mvc.application.command.Command;
import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.dispatcher.CommandHandlerDispatcher;
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSourceFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.presentation.Presenter;

@RestController
@RequestMapping("/profile-avatar/{id}")
public class ProfileAvatarController {
    private final CommandHandlerDispatcher commandHandlerDispatcher;
    private final Presenter presenter;
    
    public ProfileAvatarController(
        CommandHandlerDispatcher commandHandlerDispatcher,
        Presenter presenter
    ) {
        this.commandHandlerDispatcher = commandHandlerDispatcher;
        this.presenter = presenter;
    }
    
    @GetMapping
    public ResponseEntity<HandledResult<String>> selectProfileAvatar(
        @PathVariable("id") UUID profileId
    ) {
        Command command = ProfileAvatarCommand.select(profileId);
        HandledResult<String> result = commandHandlerDispatcher.dispatch(command);
        return presenter.present(HttpStatus.OK, result);
    }

    @PostMapping
    public ResponseEntity<HandledResult<Void>> changeProfileAvatar(
        @PathVariable("id") UUID profileId,
        @RequestParam("file") MultipartFile avatarImageFile
    ) {
        FileSource fileSource = FileSourceFactory.of(avatarImageFile);
        Command command = ProfileAvatarCommand.change(profileId, fileSource);
        HandledResult<Void> result = commandHandlerDispatcher.dispatch(command);
        return presenter.present(HttpStatus.OK, result);
    }

    @DeleteMapping
    public ResponseEntity<HandledResult<Void>> deleteProfileAvatar(
        @PathVariable("id") UUID profileId
    ) {
        Command command = ProfileAvatarCommand.delete(profileId);
        HandledResult<Void> result = commandHandlerDispatcher.dispatch(command);
        return presenter.present(HttpStatus.OK, result);
    }
}