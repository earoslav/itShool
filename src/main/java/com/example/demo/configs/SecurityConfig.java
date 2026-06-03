package com.example.demo.configs;

import com.example.demo.services.security.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Конфігурація описує правила доступу до відкритих, адмінських, викладацьких і студентських маршрутів.
// Тут також задаються сторінка логіну, BCrypt для паролів і handler, який після входу веде користувача у свій кабінет.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // Створює UserService як джерело користувачів для Spring Security.
    // Під час логіну саме цей сервіс шукає User за email з форми входу.
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService;
    }
    // Налаштовує DaoAuthenticationProvider для перевірки користувачів з бази.
    // У provider передається UserService і BCryptPasswordEncoder, тому паролі порівнюються як хеші.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    // Реєструє BCryptPasswordEncoder як bean для хешування і перевірки паролів.
    // Його використовують Spring Security та контролери, які створюють або оновлюють акаунти.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Описує доступ до URL: openSource, CSS, JS та images відкриті, а admin/teacher/student закриті ролями.
    // Також задає кастомну сторінку /login, failureUrl з output=notValid, successHandler і вихід на /openSource/homepage.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MyCustomSuccessHandler successHandler) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/openSource/**").permitAll()
                        .requestMatchers("/generalStyle.css").permitAll()
                        .requestMatchers("/mainSrcipt.js").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/student/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .anyRequest().authenticated())


                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?output=notValid")
                        .successHandler(successHandler)
                ).logout(log->log.logoutUrl("/logout").logoutSuccessUrl("/openSource/homepage"))
                .build();
    }

}
