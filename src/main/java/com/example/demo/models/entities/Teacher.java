package com.example.demo.models.entities;

import com.example.demo.models.other.Comment;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// Модель Teacher описує сутність модуля «викладачі» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
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


    // Повертає поле user об’єкта Teacher.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public User getUser() {
        return user;
    }

    // Записує поле user об’єкта Teacher.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setUser(User user) {
        this.user = user;
    }
    // Записує поле name об’єкта Teacher.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setName(String name){
        this.user.setName(name);
    }
    // Записує поле email об’єкта Teacher.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setEmail(String email){
        this.user.setEmail(email);
    }
    // Записує поле password об’єкта Teacher.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setPassword(String password){
        this.user.setPassword(password);
    }

    // Повертає поле name об’єкта Teacher.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getName(){
        return user.getName();
    }
    // Повертає поле password об’єкта Teacher.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getPassword(){
        return user.getPassword();
    }
    // Повертає поле email об’єкта Teacher.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getEmail(){
        return user.getEmail();
    }

    // Повертає короткий текстовий опис Teacher для логів і налагодження.
    // У рядок потрапляють лише ключові поля, щоб під час debug було зрозуміло, який саме об’єкт обробляється.
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
