package com.example.demo.models.thirdTables;

import com.example.demo.models.programe.Course;
import com.example.demo.models.entities.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

// The TeacherCourse model describes the "teacher courses" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teacher_course")
public class TeacherCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")

    private Teacher teacher;
    // Receives Course and Teacher dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teacher courses" module without manual object creation.
    public TeacherCourse(Course course, Teacher teacher) {
        this.course = course;
        this.teacher = teacher;
    }
}
