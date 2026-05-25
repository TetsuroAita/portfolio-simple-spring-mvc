package com.example.portfolio_simple_spring_mvc.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;

@Service
public class Presenter {

    public <T> ResponseEntity<HandledResult<T>> present(HttpStatus httpStatus, HandledResult<T> result) {

        return ResponseEntity
                .status(httpStatus)
                .header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
                .header("Pragma", "no-cache")
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}