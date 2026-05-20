package com.example.demo.models.programe;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// The Lesson model describes the "lessons" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")

    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")

    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "lesson_time")
    private LocalDateTime lessonTime;

    @Column(name = "duration")
    private float duration;


    @Column(name = "planed_date_id")
    private int timeOfTheWeek;

    @Column(name = "status", length = 30)
    private String status;
    // Receives Student, Teacher, Course, LocalDateTime, float, TimeOfTheWeek, and String dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "lessons" module without manual object creation.
    public Lesson(Student student, Teacher teacher, Course course, LocalDateTime lessonTime, float duration, int timeOfTheWeek, String status) {
        this.student = student;
        this.teacher = teacher;
        this.course = course;
        this.lessonTime = lessonTime;
        this.duration = duration;
        this.timeOfTheWeek = timeOfTheWeek;
        this.status = status;

    }
}
