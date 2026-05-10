package com.example.demo.models.entities;

import jakarta.persistence.*;
import lombok.*;

// Модель Admin описує сутність модуля «адміністратори» у базі даних або службовий об’єкт проекту.
// Поля класу читають сервіси, репозиторії та mapper-и під час створення сторінок і збереження змін.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "admin")
public class Admin{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Зовнішній ключ
    private User user;

    // Повертає поле id об’єкта Admin.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public int getId() {
        return id;
    }
    // Повертає поле name об’єкта Admin.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String  getName() {
        return user.getName();
    }
    // Повертає поле email об’єкта Admin.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getEmail(){
        return user.getEmail();
    }
    // Повертає поле password об’єкта Admin.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public String getPassword(){
        return user.getPassword();
    }
    // Повертає поле user об’єкта Admin.
    // Його читають mapper-и, сервіси або Thymeleaf-шаблони під час показу сторінок і заповнення форм.
    public User getUser() {
        return user;
    }

    // Записує поле id об’єкта Admin.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setId(int id) {
        this.id = id;
    }

    // Записує поле user об’єкта Admin.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setUser(User user) {
        this.user = user;
    }
    // Записує поле name об’єкта Admin.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setName(String name){
        user.setName(name);
    }
    // Записує поле email об’єкта Admin.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setEmail(String email){
        user.setEmail(email);
    }
    // Записує поле password об’єкта Admin.
    // Це поле приходить з форми, mapper-а або сервісу перед збереженням чи показом даних.
    public void setPassword(String password){
        user.setPassword(password);
    }
}
