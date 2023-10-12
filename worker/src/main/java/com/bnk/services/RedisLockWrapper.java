package com.bnk.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class RedisLockWrapper {
    RedisLock redisLock;
    public void lockAndExecuteTask(String key, Duration duration, Runnable runnable) {
        try{
            if(!redisLock.acquireLock(key, duration))
                return;
            runnable.run();
        } finally {
            redisLock.releaseLock(key);
        }

    }
}
