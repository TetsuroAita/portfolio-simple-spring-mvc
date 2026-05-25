package com.example.portfolio_simple_spring_mvc.application.scheduler;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteAvatarsExecutor;

@SpringBootTest(
    classes = {
        DeleteAvatarsScheduler.class,
        DeleteAvatarsSchedulerTest.TestConfig.class
    }
)
@ActiveProfiles("test")
public class DeleteAvatarsSchedulerTest {
    @Configuration
    @EnableScheduling
    static class TestConfig {}

    @MockitoSpyBean private DeleteAvatarsScheduler scheduler;
    @MockitoBean private DeleteAvatarsExecutor executor;

    @Test
    void test_do_onScheduled() {
        await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() ->
                verify(scheduler, atLeast(3)).stick()
            );
    }
}