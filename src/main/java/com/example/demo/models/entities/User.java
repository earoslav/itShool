package com.example.demo.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// The User model describes the "user accounts" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @Column(name="name")
    protected String name;
    @Column(name="email")
    protected String email;
    @Column(name="password")
    protected String password;
    @Column(name="role")
    protected String role;
    // Receives String dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "user accounts" module without manual object creation.
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Returns the authorities field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Returns the username field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public String getUsername() {
        return getEmail();
    }

    // Returns the accountNonExpired field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public boolean isAccountNonExpired() {
        return true;
    }

    // Returns the accountNonLocked field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public boolean isAccountNonLocked() {
        return true;
    }

    // Returns the credentialsNonExpired field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Returns the enabled field of the User object.
    // It is read by mappers, services, or Thymeleaf templates during page display and form filling.
    @Override

    public boolean isEnabled() {
        return true;
    }
}
