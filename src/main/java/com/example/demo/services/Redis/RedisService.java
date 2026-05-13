package com.example.demo.services.Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addIfEmpty(String key, Object val){
        if(redisTemplate.opsForValue().get(key)==null){
            redisTemplate.opsForValue().set(key, val);
        }
    }
    public void update(String key, Object val){
        redisTemplate.opsForValue().set(key, val);
    }
}
