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

// The Mail model describes the "email message" module entity in the database or a project service object.
// The class fields are read by services, repositories, and mappers when creating pages and saving changes.
public class Mail {
    private List<String> to;
    private String subject;
    private String body;



}
