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
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO TeacherStudentTimeOfTheWeekDTO переносить дані модуля «постійний розклад викладача зі студентом» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
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
