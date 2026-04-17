package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "course")

public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "cost_per_lesson")
    private Integer costPerLesson;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "course")
    private List<TeacherCourse> teacherCourses;
    @OneToMany(mappedBy = "course")
    private List<StudentCourse> studentCourses;

    public Course(String name, Integer costPerLesson, String description, List<TeacherCourse> teacherCourses, List<StudentCourse> studentCourses) {
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.description = description;
        this.teacherCourses = teacherCourses;
        this.studentCourses = studentCourses;
    }
}
