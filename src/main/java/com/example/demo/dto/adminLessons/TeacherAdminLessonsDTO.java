package com.example.demo.dto.adminLessons;

import lombok.*;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The TeacherAdminLessonsDTO DTO transfers "lessons" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
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
