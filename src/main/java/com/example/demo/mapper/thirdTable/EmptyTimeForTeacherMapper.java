package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import org.springframework.stereotype.Service;

@Service
public class EmptyTimeForTeacherMapper {
    public EmptyTimeForTeacherMapper() {
    }
    public EmptyTimesForTeacher mapETFTDTOToETFT(EmptyTimesForTeacherDTO emptyTimesForTeacherDTO){
        return UniversalMapper.generalMapper(emptyTimesForTeacherDTO, EmptyTimesForTeacher.class);
    }
    public EmptyTimesForTeacherDTO mapETFTToETFTDTO(EmptyTimesForTeacher emptyTimesForTeacher){
        return UniversalMapper.generalMapper(emptyTimesForTeacher, EmptyTimesForTeacherDTO.class);
    }
}
