package com.bnk.services;

import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

public class RedisLockService {

    ValueOperations<String, Long>  valueOperations;//это от redis
    //Long когда блокировка будет снята - что-то вроде TTL


    public boolean acquireLock(String key, Duration duration) {//не захватываем ресурс, а запрашиваем блокировку
        return false;
    }
}
