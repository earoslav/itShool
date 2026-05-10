package com.example.demo.dto.other;

import com.example.demo.dto.programe.LessonDTO;
import com.example.demo.dto.thirdTable.EmptyTimesForTeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
// DTO TimeOfTheWeekDTO переносить дані модуля «часові слоти тижня» між контролерами, формами та шаблонами.
// Клас не містить бізнес-логіки, а лише поля, які потрібні веб-рівню для показу або прийому даних.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeOfTheWeekDTO {
    private int id;
    private Integer dayOfTheWeek;
    private Integer timeOfTheDay;
    private Integer minute;
}
