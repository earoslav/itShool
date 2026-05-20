package com.example.demo.models.thirdTables;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.entities.Teacher;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// The EmptyTimesForTeacher model describes the "teacher free time" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empty_times_for_teacher")
public class EmptyTimesForTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @ManyToOne
    @JoinColumn(name = "time_id")
    private TimeOfTheWeek time;
    // Receives Teacher and TimeOfTheWeek dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teacher free time" module without manual object creation.
    public EmptyTimesForTeacher(Teacher teacher, TimeOfTheWeek time) {

        this.time = time;
    }
}
