package com.example.demo.dto.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// This class separates the external representation of information from JPA entities.
// It contains only the fields required for display or form processing.

// The UserDTO DTO transfers "user accounts" module data between controllers, forms, and templates.
// The class contains no business logic, only fields required by the web layer for displaying or receiving data.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    protected int id;

    protected String name;
    protected String email;
    @JsonIgnore
    protected String password;

    protected String role;

}
