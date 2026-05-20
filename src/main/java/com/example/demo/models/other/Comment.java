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

// The Comment model describes the "student comments" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
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
    // Receives Student, Teacher, String, and int dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "student comments" module without manual object creation.
    public Comment(Student student, Teacher teacher, String commentText, int ratingOfStars) {
        this.student = student;
        this.teacher = teacher;
        this.commentText = commentText;
        this.ratingOfStars = ratingOfStars;
    }
}
