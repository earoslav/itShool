package com.example.demo.dto.thirdTable;

import com.example.demo.dto.other.TimeOfTheWeekDTO;
import com.example.demo.dto.entities.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The EmptyTimesForTeacherDTO DTO transfers "teacher free time" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmptyTimesForTeacherDTO {
    private int id;
    private TimeOfTheWeekDTO time;
}
