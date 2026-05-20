package com.example.demo.mapper.other;

import com.example.demo.dto.adminLessons.TimeOfTheWeekAdminLessonsDTO;
import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.services.other.TimeOfTheWeekService;
import org.springframework.stereotype.Service;

// TimeOfTheWeekMapper converts "weekly time slots" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class TimeOfTheWeekMapper {
    private TimeOfTheWeekService theWeekService;
    // Creates an empty TimeOfTheWeekMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public TimeOfTheWeekMapper(TimeOfTheWeekService theWeekService) {
        this.theWeekService = theWeekService;
    }
    // Converts TTheWeekDTO to TTheWeek using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TimeOfTheWeek mapTTheWeekDTOToTTheWeek(TimeOfTheWeekDTO timeOfTheWeekDTO){
        return UniversalMapper.generalMapper(timeOfTheWeekDTO, TimeOfTheWeek.class);
    }
    // Converts TTheWeek to TTheWeekDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TimeOfTheWeekDTO mapTTheWeekToTTheWeekDTO(TimeOfTheWeek time){
        return UniversalMapper.generalMapper(time, TimeOfTheWeekDTO.class);
    }
    public TimeOfTheWeekDTO mapIntToTTheWeekDTO(int id){
        TimeOfTheWeek time = theWeekService.getById(id);
        return new TimeOfTheWeekDTO(time.getId(), time.getDayOfTheWeek(), time.getTimeOfTheDay(), time.getMinute());
    }
    public TimeOfTheWeekAdminLessonsDTO mapIntToTimeOfTheWeekAdminLessonsDTO(int id){
        TimeOfTheWeek time = theWeekService.getById(id);
        return new TimeOfTheWeekAdminLessonsDTO(time.getId(), time.getDayOfTheWeek(), time.getTimeOfTheDay(), time.getMinute());
    }
    public int mapTTheWeekDTOToInt(TimeOfTheWeekDTO time){
        return time.getId();
    }
    public int mapTimeOfTheWeekAdminLessonsDTOToInt(TimeOfTheWeekAdminLessonsDTO time){
        return time.getId();
    }

}
