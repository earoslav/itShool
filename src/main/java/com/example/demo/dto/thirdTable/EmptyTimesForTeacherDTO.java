package com.example.demo.dto.thirdTable;

import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.dto.entities.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmptyTimesForTeacherDTO {
    private int id;
    private TimeOfTheWeekDTO time;
}
