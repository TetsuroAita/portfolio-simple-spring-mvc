package com.example.portfolio_simple_spring_mvc.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;

@ExtendWith(MockitoExtension.class)
public class PresenterTest {
    private Presenter presenter;
    
    @Mock
    private HandledResult<?> result;

    @BeforeEach
    void setUp() {
        presenter = new Presenter();
    }
    
    @Test
    void testPresenter() {
        ResponseEntity<?> expected =
            ResponseEntity
                .status(HttpStatus.OK)
                .header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
                .header("Pragma", "no-cache")
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);

        ResponseEntity<?> response = presenter.present(HttpStatus.OK, result);

        assertThat(response).isEqualTo(expected);
    }
}
