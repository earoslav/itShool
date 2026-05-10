package com.example.demo.controlers.auth;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login(@RequestParam(value = "output", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("output", error);
        }
        return "auth/login";
    }
}
