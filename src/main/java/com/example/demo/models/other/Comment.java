package com.example.demo.models.other;

import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @Column(name = "comment_text")
    private String commentText;
    @Column(name = "rating_of_stars")
    private int ratingOfStars;

    public Comment(Student student, Teacher teacher, String commentText, int ratingOfStars) {
        this.student = student;
        this.teacher = teacher;
        this.commentText = commentText;
        this.ratingOfStars = ratingOfStars;
    }
}
