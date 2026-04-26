package com.example.demo.models.entities;

import com.example.demo.models.other.Comment;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.StudentCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setName(String name){
        user.setName(name);
    }
    public void setEmail(String email){
        user.setEmail(email);
    }
    public void setPassword(String password){
        user.setPassword(password);
    }
    public String getName(){
        return user.getName();
    }
    public String getPassword(){
        return user.getPassword();
    }
    public String getEmail(){
        return user.getEmail();
    }

}
