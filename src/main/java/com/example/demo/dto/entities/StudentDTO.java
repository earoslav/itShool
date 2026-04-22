package com.example.demo.dto.entities;

import com.example.demo.dto.other.CommentDTO;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDTO {
    private int id;
    private String name;
    private Integer age;
    private String comment;
    private String email;
    private String phoneNumber;


}
