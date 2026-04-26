package com.example.demo.models.entities;

import jakarta.persistence.*;
import lombok.*;

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

    public int getId() {
        return id;
    }
    public String  getName() {
        return user.getName();
    }
    public String getEmail(){
        return user.getEmail();
    }
    public String getPassword(){
        return user.getPassword();
    }
    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setName(String name){
        user.setName(name);
    }
    public void setEmail(String email){
        user.setEmail(email);
    }
    public void setPassword(String password){
        user.setPassword(password);
    }
}
