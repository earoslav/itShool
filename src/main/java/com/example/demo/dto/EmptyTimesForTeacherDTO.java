package com.example.demo.dto;

import com.example.demo.models.TimeOfTheWeek;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmptyTimesForTeacherDTO {
    private TeacherDTO teacher;


    private TimeOfTheWeekDTO time;
}
