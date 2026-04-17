package com.example.demo.dto;

import com.example.demo.models.Comment;
import com.example.demo.models.Lesson;
import com.example.demo.models.StudentCourse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDTO {
    private String name;

    private Integer age;

    private String comment;

    private List<CommentDTO> myComments;

}
