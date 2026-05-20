package com.example.demo.services.other;

import com.example.demo.repositories.other.TimesRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TimesService {
    private TimesRepository timesRepository;
    private RedisTemplate<String, Object> redisTemplate;
    public TimesService(TimesRepository timesRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.timesRepository = timesRepository;
        this.redisTemplate = redisTemplate;
    }
    public String getTimes(){
        String obj;
        if(redisTemplate.opsForValue().get("times") == null){
            obj = timesRepository.findById(1).getTimes();
            redisTemplate.opsForValue().set("times", obj);
        } else {
            obj = (String) redisTemplate.opsForValue().get("times");
        }
        return obj;
    }
}
