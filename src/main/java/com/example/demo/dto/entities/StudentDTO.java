package com.example.demo.dto.entities;

import com.example.demo.dto.other.CommentDTO;
import com.example.demo.dto.programe.LessonSimpleDTO;
import com.example.demo.dto.thirdTable.TeacherStudentTimeOfTheWeekSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// The StudentDTO DTO transfers "students" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDTO {
    private int id;
    private Integer age;
    private String comment;
    private UserDTO user;
    private String phoneNumber;
    private List<LessonSimpleDTO> lessons;
    private List<TeacherStudentTimeOfTheWeekSimpleDTO> tswList;
}
