package com.example.demo.mapper.thirdTable;

import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import org.springframework.stereotype.Service;

// EmptyTimeForTeacherMapper converts "teachers" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class EmptyTimeForTeacherMapper {
    // Creates an empty EmptyTimeForTeacherMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public EmptyTimeForTeacherMapper() {
    }
    // Converts ETFTDTO to ETFT using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public EmptyTimesForTeacher mapETFTDTOToETFT(EmptyTimesForTeacherDTO emptyTimesForTeacherDTO){
        return UniversalMapper.generalMapper(emptyTimesForTeacherDTO, EmptyTimesForTeacher.class);
    }
    // Converts ETFT to ETFTDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public EmptyTimesForTeacherDTO mapETFTToETFTDTO(EmptyTimesForTeacher emptyTimesForTeacher){
        return UniversalMapper.generalMapper(emptyTimesForTeacher, EmptyTimesForTeacherDTO.class);
    }
}
