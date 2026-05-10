package com.example.demo.dto.entities;

import com.example.demo.dto.other.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO StudentDTOWithComments переносить дані модуля «студенти» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
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
