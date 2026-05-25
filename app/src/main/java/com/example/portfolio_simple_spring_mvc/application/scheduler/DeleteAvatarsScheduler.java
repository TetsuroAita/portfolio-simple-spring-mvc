package com.example.portfolio_simple_spring_mvc.application.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteAvatarsExecutor;

@Component
public class DeleteAvatarsScheduler implements Scheduler {
    private final Logger logger = LoggerFactory.getLogger(DeleteAvatarsScheduler.class);
    private final DeleteAvatarsExecutor executor;

    public DeleteAvatarsScheduler(
        DeleteAvatarsExecutor executor
    ) {
        this.executor = executor;
    }

    @Transactional
    @Scheduled(
        fixedDelayString = "${scheduler.delete-avatars.fixed-rate-ms}",
        initialDelayString = "${scheduler.delete-avatars.initial-delay}"
    )
    public void stick() {
        long count = executor.execute();
        logger.debug("{}件、削除しました。", count);
    }
}
