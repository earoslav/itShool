package com.example.demo.models.thirdTables;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Teacher;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Модель EmptyTimesForTeacher описує сутність модуля «вільний час викладача» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empty_times_for_teacher")
public class EmptyTimesForTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @ManyToOne
    @JoinColumn(name = "time_id")
    private TimeOfTheWeek time;
    // Отримує через Spring залежності Teacher, TimeOfTheWeek.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «вільний час викладача» без ручного створення об’єктів.
    public EmptyTimesForTeacher(Teacher teacher, TimeOfTheWeek time) {

        this.time = time;
    }
}
