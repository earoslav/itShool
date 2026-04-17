package com.example.demo.dto.adminLessons;

import com.example.demo.models.TimeOfTheWeek;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeacherAdminLessonsDTO {
    private int id;

    private String name;


    private Integer age;



    private String comment;

    private boolean approved;
    private String notTakenTimes;


}
