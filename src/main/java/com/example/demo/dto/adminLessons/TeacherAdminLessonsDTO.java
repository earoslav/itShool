package com.example.demo.dto.adminLessons;

import lombok.*;

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
