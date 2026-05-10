package com.example.demo.models.email;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

// Модель Mail описує сутність модуля «email-повідомлення» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
public class Mail {
    private List<String> to;
    private String subject;
    private String body;



}
