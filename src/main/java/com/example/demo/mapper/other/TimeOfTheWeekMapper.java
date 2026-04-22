package com.example.demo.mapper.other;

import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.TimeOfTheWeek;
import org.springframework.stereotype.Service;

@Service
public class TimeOfTheWeekMapper {
    public TimeOfTheWeekMapper() {
    }
    public TimeOfTheWeek mapTTheWeekDTOToTTheWeek(TimeOfTheWeekDTO timeOfTheWeekDTO){
        return UniversalMapper.generalMapper(timeOfTheWeekDTO, TimeOfTheWeek.class);
    }
    public TimeOfTheWeekDTO mapTTheWeekToTTheWeekDTO(TimeOfTheWeek time){
        return UniversalMapper.generalMapper(time, TimeOfTheWeekDTO.class);
    }

}
