package com.example.demo.dto.thirdTable;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.other.TimeOfTheWeekDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The TeacherStudentTimeOfTheWeekDTO DTO transfers "teacher-student persistent schedule" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherStudentTimeOfTheWeekDTO {
    private int id;
    private TeacherDTO teacher;
    private StudentDTO student;
    private TimeOfTheWeekDTO timeOfTheWeek;
}
