package com.example.demo.dto.entities;

import com.example.demo.dto.other.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The StudentDTOWithComments DTO transfers "students" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDTOWithComments {
    private int id;
    private String name;
    private Integer age;
    private String comment;
    private String email;
    private String phoneNumber;

    private List<CommentDTO> myComments;

}
