package com.example.demo.mapper.other;

import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.TimeOfTheWeek;
import org.springframework.stereotype.Service;

// Mapper TimeOfTheWeekMapper перетворює об’єкти модуля «часові слоти тижня» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class TimeOfTheWeekMapper {
    // Створює порожній TimeOfTheWeekMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public TimeOfTheWeekMapper() {
    }
    // Перетворює TTheWeekDTO у TTheWeek через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TimeOfTheWeek mapTTheWeekDTOToTTheWeek(TimeOfTheWeekDTO timeOfTheWeekDTO){
        return UniversalMapper.generalMapper(timeOfTheWeekDTO, TimeOfTheWeek.class);
    }
    // Перетворює TTheWeek у TTheWeekDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TimeOfTheWeekDTO mapTTheWeekToTTheWeekDTO(TimeOfTheWeek time){
        return UniversalMapper.generalMapper(time, TimeOfTheWeekDTO.class);
    }

}
