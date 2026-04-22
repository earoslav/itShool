package com.example.demo.dto.other;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private int id;
    private String commentText;
    private int ratingOfStars;
}
