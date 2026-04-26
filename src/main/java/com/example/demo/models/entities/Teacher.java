package com.example.demo.models.entities;

import com.example.demo.models.other.Comment;
import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherCourse;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setName(String name){
        this.user.setName(name);
    }
    public void setEmail(String email){
        this.user.setEmail(email);
    }
    public void setPassword(String password){
        this.user.setPassword(password);
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
