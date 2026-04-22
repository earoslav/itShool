package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import org.springframework.stereotype.Service;

@Service
public class TSWMapper {
    public TSWMapper() {
    }
    public TeacherStudentTimeOfTheWeek mapTSWDTOToTSW(TeacherStudentTimeOfTheWeekDTO tswDTO){
        return UniversalMapper.generalMapper(tswDTO, TeacherStudentTimeOfTheWeek.class);
    }
    public TeacherStudentTimeOfTheWeekDTO mapTSWToTSWDTO(TeacherStudentTimeOfTheWeek tsw){
        return UniversalMapper.generalMapper(tsw, TeacherStudentTimeOfTheWeekDTO.class);
    }
}
