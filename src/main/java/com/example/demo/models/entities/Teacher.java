package com.example.demo.models.entities;

import com.example.demo.models.other.Comment;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// The Teacher model describes the "teachers" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "age")
    private Integer age;



    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "tg_username", length = 30)
    private String tgUsername;
    @Column(name = "approved")
    private int approved;
    @Column(name = "unpaid_money")
    private float unpaidMoney;
    @Column(name = "free_time_ids")
    private String freeTimeIds;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherCourse> teacherCourses;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherStudentTimeOfTheWeek> tswList;


    @OneToMany(mappedBy = "teacher")

    private List<Lesson> lessons;
    @OneToMany(mappedBy = "teacher")
    private List<Comment> myComments;



    // Returns the user field of the Teacher object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public User getUser() {
        return user;
    }

    // Sets the user field of the Teacher object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setUser(User user) {
        this.user = user;
    }
    // Sets the name field of the Teacher object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setName(String name){
        this.user.setName(name);
    }
    // Sets the email field of the Teacher object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setEmail(String email){
        this.user.setEmail(email);
    }
    // Sets the password field of the Teacher object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setPassword(String password){
        this.user.setPassword(password);
    }

    // Returns the name field of the Teacher object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getName(){
        return user.getName();
    }
    // Returns the password field of the Teacher object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getPassword(){
        return user.getPassword();
    }
    // Returns the email field of the Teacher object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getEmail(){
        return user.getEmail();
    }

    // Returns a short text description of the Teacher for logs and debugging.
    // Only key fields are included in the string so it's clear which object is being processed during debugging.
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
