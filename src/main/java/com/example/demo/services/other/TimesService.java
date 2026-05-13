package com.example.demo.services.other;

import com.example.demo.repositories.other.TimesRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TimesService {
    private TimesRepository timesRepository;
    private RedisTemplate redis;
    public TimesService(TimesRepository timesRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.timesRepository = timesRepository;
        this.redis = redis;
    }
    public String getTimes(){
        String obj;
        if(redis.opsForValue().get("times") == null){
            obj = timesRepository.findById(1).getTimes();
            redis.opsForValue().set("times", obj);
        } else {
            obj = (String) redis.opsForValue().get("times");
        }
        return obj;
    }
}
