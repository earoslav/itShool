package com.example.demo.models.other;

import com.example.demo.models.programe.Lesson;
import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
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

    public TimeOfTheWeek(Integer dayOfTheWeek, Integer timeOfTheDay, List<EmptyTimesForTeacher> emptyTimesForTeachers, List<Lesson> lessons) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.timeOfTheDay = timeOfTheDay;
        this.emptyTimesForTeachers = emptyTimesForTeachers;
        this.lessons = lessons;
    }

}
