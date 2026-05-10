package com.example.demo.services.security;

import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Сервіс UserService містить бізнес-операції для модуля «акаунти користувачів».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    // Порожній конструктор залишений для створення сервісу Spring-ом.
    // UserRepository інжектиться через поле @Autowired і далі використовується для логіну та створення акаунтів.
    public UserService() {
    }
    // Шукає User за email, який Spring Security отримав з форми логіну.
    // Повернений UserDetails використовується для перевірки пароля і ролей користувача.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).get();
    }
    // Перевіряє, чи в базі вже є акаунт з таким email.
    // Цей boolean потрібен формам реєстрації та адмінським формам, щоб не створити дубль користувача.
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    // Зберігає новий User після кодування пароля і присвоєння ролі.
    // Метод викликають контролери створення студента або викладача перед збереженням профілю.
    public void create(User user){
        userRepository.save(user);
    }
}
