package com.example.demo.dto.adminLessons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Такий клас відокремлює зовнішнє представлення інформації від JPA-сутностей.
// У ньому залишаються тільки поля, які потрібні для відображення або обробки форми.

// DTO TimeOfTheWeekAdminLessonsDTO переносить дані модуля «часові слоти тижня» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeOfTheWeekAdminLessonsDTO {
    private int id;
    private Integer dayOfTheWeek;
    private Integer timeOfTheDay;
    private Integer minute;


}
