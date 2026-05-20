package com.example.demo.models.programe;

import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

// The Course model describes the "courses" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "cost_per_lesson")
    private Integer costPerLesson;
    @Column(name = "teacher_share_per_lesson")
    private Integer teacherShare;

    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "program")
    private String program;
    @JsonIgnore
    @OneToMany(mappedBy = "course")

    private List<TeacherCourse> teacherCourses;


    @OneToMany(mappedBy = "course")
    private List<TeacherStudentTimeOfTheWeek> tswList;
    // Receives String, Integer, List<TeacherCourse>, List<StudentCourse>, and List<TeacherStudentTimeOfTheWeek> dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "courses" module without manual object creation.
    public Course(String name, Integer costPerLesson, Integer teacherShare, String description, List<TeacherCourse> teacherCourses,  List<TeacherStudentTimeOfTheWeek> tswList) {
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
        this.teacherCourses = teacherCourses;

        this.tswList = tswList;
    }
    // Receives String and Integer dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "courses" module without manual object creation.
    public Course(String name, Integer costPerLesson, Integer teacherShare, String description) {
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
    }

    public Course(int id, String name, Integer costPerLesson, Integer teacherShare, String description, String program) {
        this.id = id;
        this.name = name;
        this.costPerLesson = costPerLesson;
        this.teacherShare = teacherShare;
        this.description = description;
        this.program = program;
    }
}
