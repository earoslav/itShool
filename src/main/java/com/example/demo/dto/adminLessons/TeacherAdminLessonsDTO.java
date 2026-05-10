package com.example.demo.dto.adminLessons;

import lombok.*;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO TeacherAdminLessonsDTO переносить дані модуля «уроки» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
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
