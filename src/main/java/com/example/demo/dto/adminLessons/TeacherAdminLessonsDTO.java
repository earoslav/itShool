package com.example.demo.dto.adminLessons;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeacherAdminLessonsDTO {
    private int id;
    private Integer age;
    private UserAdminLessonsDTO user;
    private String comment;
    private int approved;
    private String notTakenTimes;


}
