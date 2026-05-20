package com.example.demo.models.other;

import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// The TimeOfTheWeek model describes the "weekly time slots" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "time_of_the_week")
public class TimeOfTheWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "day_of_the_week")
    private Integer dayOfTheWeek;
    @Column(name = "minute")
    private Integer minute;

    @Column(name = "time_of_the_day")
    private Integer timeOfTheDay;

    @OneToMany(mappedBy = "time")

    private List<EmptyTimesForTeacher> emptyTimesForTeachers;
    @OneToMany(mappedBy = "timeOfTheWeek")

    private List<TeacherStudentTimeOfTheWeek> tswList;

    @OneToMany(mappedBy = "timeOfTheWeek")

    private List<Lesson> lessons;
    // Receives Integer, List<EmptyTimesForTeacher>, and List<Lesson> dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "weekly time slots" module without manual object creation.
    public TimeOfTheWeek(Integer dayOfTheWeek, Integer timeOfTheDay, List<EmptyTimesForTeacher> emptyTimesForTeachers, List<Lesson> lessons) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.timeOfTheDay = timeOfTheDay;
        this.emptyTimesForTeachers = emptyTimesForTeachers;
        this.lessons = lessons;
    }

    public TimeOfTheWeek(Integer id, Integer dayOfTheWeek, Integer timeOfTheDay, Integer minute) {
        this.id = id;
        this.dayOfTheWeek = dayOfTheWeek;
        this.minute = minute;
        this.timeOfTheDay = timeOfTheDay;
    }
}
