package com.example.demo.dto.thirdTable;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
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
public class TeacherStudentTimeOfTheWeekSimpleDTO {
    private int id;

}
