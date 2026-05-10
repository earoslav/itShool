package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import org.springframework.stereotype.Service;

// Mapper TSWMapper перетворює об’єкти модуля «постійний розклад викладача зі студентом» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class TSWMapper {
    // Створює порожній TSWMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public TSWMapper() {
    }
    // Перетворює TSWDTO у TSW через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TeacherStudentTimeOfTheWeek mapTSWDTOToTSW(TeacherStudentTimeOfTheWeekDTO tswDTO){
        return UniversalMapper.generalMapper(tswDTO, TeacherStudentTimeOfTheWeek.class);
    }
    // Перетворює TSW у TSWDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public TeacherStudentTimeOfTheWeekDTO mapTSWToTSWDTO(TeacherStudentTimeOfTheWeek tsw){
        return UniversalMapper.generalMapper(tsw, TeacherStudentTimeOfTheWeekDTO.class);
    }
}
