package com.example.demo.dto.entities;

import com.example.demo.dto.other.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
