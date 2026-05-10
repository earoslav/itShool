package com.example.demo.controlers.auth;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Контролер AuthController обслуговує веб-сторінки модуля «авторизація».
// Методи нижче приймають параметри з URL або форм, викликають сервіси проекту і повертають потрібні Thymeleaf-шаблони чи redirect-и.
@Controller
public class AuthController {
    // Відкриває маршрут GET /login і готує дані для шаблону "auth/login".
    // У Model додає "output".
    @GetMapping("/login")
    public String login(@RequestParam(value = "output", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("output", error);
        }
        return "auth/login";
    }
}
