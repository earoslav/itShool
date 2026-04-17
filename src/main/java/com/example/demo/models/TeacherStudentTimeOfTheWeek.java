package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "teacherstudenttimeoftheweek")
public class TeacherStudentTimeOfTheWeek {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "time_of_the_week_id")
    private TimeOfTheWeek timeOfTheWeek;

    public TeacherStudentTimeOfTheWeek(Teacher teacher, Student student, TimeOfTheWeek timeOfTheWeek) {
        this.teacher = teacher;
        this.student = student;
        this.timeOfTheWeek = timeOfTheWeek;
    }
}
