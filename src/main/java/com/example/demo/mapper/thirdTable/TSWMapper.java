package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import org.springframework.stereotype.Service;

// TSWMapper converts "teacher-student persistent schedule" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class TSWMapper {
    // Creates an empty TSWMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public TSWMapper() {
    }
    // Converts TSWDTO to TSW using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TeacherStudentTimeOfTheWeek mapTSWDTOToTSW(TeacherStudentTimeOfTheWeekDTO tswDTO){
        return UniversalMapper.generalMapper(tswDTO, TeacherStudentTimeOfTheWeek.class);
    }
    // Converts TSW to TSWDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public TeacherStudentTimeOfTheWeekDTO mapTSWToTSWDTO(TeacherStudentTimeOfTheWeek tsw){
        return UniversalMapper.generalMapper(tsw, TeacherStudentTimeOfTheWeekDTO.class);
    }
}
