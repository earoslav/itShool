package com.example.demo.models.entities;

import jakarta.persistence.*;
import lombok.*;

// The Admin model describes the "admins" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
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

    // Returns the id field of the Admin object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public int getId() {
        return id;
    }
    // Returns the name field of the Admin object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String  getName() {
        return user.getName();
    }
    // Returns the email field of the Admin object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getEmail(){
        return user.getEmail();
    }
    // Returns the password field of the Admin object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public String getPassword(){
        return user.getPassword();
    }
    // Returns the user field of the Admin object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    public User getUser() {
        return user;
    }

    // Sets the id field of the Admin object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setId(int id) {
        this.id = id;
    }

    // Sets the user field of the Admin object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setUser(User user) {
        this.user = user;
    }
    // Sets the name field of the Admin object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setName(String name){
        user.setName(name);
    }
    // Sets the email field of the Admin object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setEmail(String email){
        user.setEmail(email);
    }
    // Sets the password field of the Admin object.
    // This field comes from a form, mapper, or service before saving or displaying data.
    public void setPassword(String password){
        user.setPassword(password);
    }
}
