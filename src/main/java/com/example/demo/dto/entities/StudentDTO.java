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
// DTO StudentDTO переносить дані модуля «студенти» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
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


}
