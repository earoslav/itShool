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

// Модель Student описує сутність модуля «студенти» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
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

    // Повертає поле user об’єкта Student.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public User getUser() {
        return user;
    }

    // Записує поле user об’єкта Student.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setUser(User user) {
        this.user = user;
    }
    // Записує поле name об’єкта Student.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setName(String name){
        user.setName(name);
    }
    // Записує поле email об’єкта Student.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setEmail(String email){
        user.setEmail(email);
    }
    // Записує поле password об’єкта Student.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setPassword(String password){
        user.setPassword(password);
    }
    // Повертає поле name об’єкта Student.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getName(){
        return user.getName();
    }
    // Повертає поле password об’єкта Student.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getPassword(){
        return user.getPassword();
    }
    // Повертає поле email об’єкта Student.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getEmail(){
        return user.getEmail();
    }

}
