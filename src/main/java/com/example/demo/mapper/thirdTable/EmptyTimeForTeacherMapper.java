package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import org.springframework.stereotype.Service;

// Mapper EmptyTimeForTeacherMapper перетворює об’єкти модуля «викладачі» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class EmptyTimeForTeacherMapper {
    // Створює порожній EmptyTimeForTeacherMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public EmptyTimeForTeacherMapper() {
    }
    // Перетворює ETFTDTO у ETFT через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public EmptyTimesForTeacher mapETFTDTOToETFT(EmptyTimesForTeacherDTO emptyTimesForTeacherDTO){
        return UniversalMapper.generalMapper(emptyTimesForTeacherDTO, EmptyTimesForTeacher.class);
    }
    // Перетворює ETFT у ETFTDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public EmptyTimesForTeacherDTO mapETFTToETFTDTO(EmptyTimesForTeacher emptyTimesForTeacher){
        return UniversalMapper.generalMapper(emptyTimesForTeacher, EmptyTimesForTeacherDTO.class);
    }
}
