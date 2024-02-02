package org.iptime.yoon.blog.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${INSTANCE_TYPE:TOP_OF_HOUR}")
    private String instanceType;



    // export INSTANCE_TYPE=EVERY_THIRTY_MINUTES
    // docker run -e INSTANCE_TYPE=EVERY_THIRTY_MINUTES my-spring-boot-app
    private void removeExpiredToken(){
        refreshTokenService.removeExpiredTokens();
        log.info("Clear Expired Refresh Tokens");
    }

    @Scheduled(cron = "0 0 * * * *", zone = "UTC")
    public void removeExpiredTokensOnTheHour(){
        if("TOP_OF_HOUR".equals(instanceType)) {
            removeExpiredToken();
        }
    }

    @Scheduled(cron = "0 30 * * * *", zone = "UTC")
    public void removeExpiredTokensEveryThirtyMinutes(){
        if("EVERY_THIRTY_MINUTES".equals(instanceType)) {
            removeExpiredToken();
        }
    }
}
