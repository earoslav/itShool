package com.example.demo.models.entities;

import com.example.demo.models.other.Comment;
import com.example.demo.models.programe.Lesson;
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

// The Student model describes the "students" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
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
    private List<Comment> myComments;

    @OneToMany(mappedBy = "student")
    private List<Lesson> lessons;

    // Returns the user field of the Student object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public User getUser() {
        return user;
    }

    // Sets the user field of the Student object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setUser(User user) {
        this.user = user;
    }
    // Sets the name field of the Student object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setName(String name){
        user.setName(name);
    }
    // Sets the email field of the Student object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setEmail(String email){
        user.setEmail(email);
    }
    // Sets the password field of the Student object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setPassword(String password){
        user.setPassword(password);
    }
    // Returns the name field of the Student object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getName(){
        return user.getName();
    }
    // Returns the password field of the Student object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getPassword(){
        return user.getPassword();
    }
    // Returns the email field of the Student object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getEmail(){
        return user.getEmail();
    }

}
