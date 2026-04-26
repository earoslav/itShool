package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentAdminLessonsDTO {
    private int id;

    private UserAdminLessonsDTO user;

    private Integer age;


}
