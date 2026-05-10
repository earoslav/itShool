package com.example.demo.dto.other;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO CommentDTO переносить дані модуля «коментарі студентів» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private int id;
    private String commentText;
    private int ratingOfStars;
}
