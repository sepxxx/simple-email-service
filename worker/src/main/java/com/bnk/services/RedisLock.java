package com.bnk.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class RedisLock {

    private static final String LOCK_FORMAT = "%s::lock";
    ValueOperations<String, Long>  valueOperations;//это от redis
    //Long когда блокировка будет снята - что-то вроде TTL
    RedisTemplate<String, Long> redisTemplate;
    public boolean acquireLock(String key, Duration duration) {//не захватываем ресурс, а запрашиваем блокировку

        String lockKey = getLockKey(key);
        Long expiresAtMillis = valueOperations.get(lockKey);
        Long currentTimeMillis = System.currentTimeMillis();
        if (Objects.nonNull(expiresAtMillis)) {
            if(currentTimeMillis<=expiresAtMillis)
                return false; //еще не можем занять задачу
            redisTemplate.delete(lockKey);//освобождаем задачу
        }



        return Optional.ofNullable(
                valueOperations.setIfAbsent(lockKey,
                        currentTimeMillis+duration.toMillis()
                        )
        ).orElse(false);
    }
    public void releaseLock(String key) {
        String lockKey = getLockKey(key);
        redisTemplate.delete(key);
    }
    private static String getLockKey(String key) {
        return LOCK_FORMAT.formatted(key);
    }
}
