package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")

public class Student {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @OneToMany(mappedBy = "student")
    private List<TeacherStudentTimeOfTheWeek> tswList;


    @Column(name = "age")
    private Integer age;




    @Column(name = "phone_number", length = 15)
    private String phoneNumber;


    @Column(name = "comment", length = 500)
    private String comment;

    @OneToMany(mappedBy = "student")
    private List<StudentCourse> studentCourses;
    @OneToMany(mappedBy = "student")
    private List<Comment> myComments;


    @OneToMany(mappedBy = "student")
    private List<Lesson> lessons;


}
