package com.example.demo.dto.thirdTable;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.other.TimeOfTheWeek;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherStudentTimeOfTheWeekDTO {
    private int id;
    private Teacher teacher;
    private Student student;
    private TimeOfTheWeek timeOfTheWeek;
}
