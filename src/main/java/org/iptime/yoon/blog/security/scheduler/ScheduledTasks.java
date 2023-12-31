package org.iptime.yoon.blog.security.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author rival
 * @since 2023-08-17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final RefreshTokenService refreshTokenService;
    @Scheduled(fixedRate = 3600000)
    public void removeExpiredTokens(){
        refreshTokenService.removeExpiredTokens();
        log.info("Clear Expired Refresh Tokens");
    }
}
