package com.example.demo.dto.thirdTable;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.programe.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO StudentCourseDTO переносить дані модуля «курси студента» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentCourseDTO {
    private int id;
    private CourseDTO course;
}
