package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher{
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

    @Column(name = "age")
    private Integer age;



    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "tg_username", length = 30)
    private String tgUsername;
    @Column(name = "approved")
    private boolean approved;



    @OneToMany(mappedBy = "teacher")

    private List<TeacherCourse> teacherCourses;
    @OneToMany(mappedBy = "teacher")
    private List<TeacherStudentTimeOfTheWeek> tswList;
    @OneToMany(mappedBy = "teacher")

    private List<EmptyTimesForTeacher> emptyTimesForTeachers;

    @OneToMany(mappedBy = "teacher")

    private List<Lesson> lessons;
    @OneToMany(mappedBy = "teacher")
    private List<Comment> myComments;



    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", age=" + age +
                ", comment='" + comment + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", tgUsername='" + tgUsername + '\'' +
                ", approved=" + approved +
                '}';
    }
}
