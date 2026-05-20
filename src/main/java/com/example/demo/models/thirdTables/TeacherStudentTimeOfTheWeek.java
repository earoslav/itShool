package com.example.demo.models.thirdTables;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// The TeacherStudentTimeOfTheWeek model describes the "teacher-student persistent schedule" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
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
    @Column(name = "time_of_the_week_id")
    private int timeOfTheWeek;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    // Receives Teacher, Student, TimeOfTheWeek, and Course dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teacher-student persistent schedule" module without manual object creation.
    public TeacherStudentTimeOfTheWeek(Teacher teacher, Student student, int timeOfTheWeek, Course course) {
        this.teacher = teacher;
        this.student = student;
        this.timeOfTheWeek = timeOfTheWeek;
        this.course = course;
    }
}
