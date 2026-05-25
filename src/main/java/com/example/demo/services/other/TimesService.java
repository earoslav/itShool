package com.example.demo.services.other;

import com.example.demo.repositories.other.TimesRepository;
import org.springframework.stereotype.Service;

@Service
public class TimesService {
    private TimesRepository timesRepository;
    public TimesService(TimesRepository timesRepository) {
        this.timesRepository = timesRepository;
    }
    public String getTimes(){
        return timesRepository.findById(1).getTimes();
    }
}
