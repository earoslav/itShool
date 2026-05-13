package com.example.demo.services.security;

import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Сервіс UserService містить бізнес-операції для модуля «акаунти користувачів».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RedisTemplate redis;

    @Autowired
    public UserService(UserRepository userRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.userRepository = userRepository;
        this.redis = redis;
    }

    // Шукає User за email, який Spring Security отримав з форми логіну.
    // Повернений UserDetails використовується для перевірки пароля і ролей користувача.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails obj;
        if(redis.opsForValue().get("userByUsername"+username) == null){
            obj = userRepository.findByEmail(username).get();
            redis.opsForValue().set("userByUsername"+username, obj);
        } else {
            obj = (UserDetails) redis.opsForValue().get("userByUsername"+username);
        }
        return obj;
    }
    // Перевіряє, чи в базі вже є акаунт з таким email.
    // Цей boolean потрібен формам реєстрації та адмінським формам, щоб не створити дубль користувача.
    public boolean checkIfExistsByEmail(String email){
        Object obj;
        if(redis.opsForValue().get("userByEmail"+email) == null){
            obj = userRepository.findByEmail(email).orElse(null);
            redis.opsForValue().set("userByEmail"+email, obj);
        } else {
            obj = redis.opsForValue().get("userByEmail"+email);
        }
        return obj != null;
    }
    // Зберігає новий User після кодування пароля і присвоєння ролі.
    // Метод викликають контролери створення студента або викладача перед збереженням профілю.
    public void create(User user){
        userRepository.save(user);
        redis.delete("userByEmail"+user.getEmail());
        redis.delete("userByUsername"+user.getEmail());
    }
}
