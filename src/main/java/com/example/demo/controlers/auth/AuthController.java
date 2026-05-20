package com.example.demo.controlers.auth;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// AuthController handles web pages for the "authorization" module.
// The methods below accept parameters from URLs or forms, call project services, and return the required Thymeleaf templates or redirects.
@Controller
public class AuthController {
    // Opens the GET /login route and prepares data for the "auth/login" template.
    // Adds "output" to the Model.
    @GetMapping("/login")
    public String login(@RequestParam(value = "output", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("output", error);
        }
        return "auth/login";
    }
}
