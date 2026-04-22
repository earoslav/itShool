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

public class Mail {
    private List<String> to;
    private String subject;
    private String body;



}
