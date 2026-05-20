package com.example.demo.services.security;

import com.example.demo.dto.entities.UserDTO;
import com.example.demo.mapper.entity.UserMapper;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserService contains business operations for the "user accounts" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    // Searches for a User by email, which Spring Security receives from the login form.
    // The result is cached in Redis to avoid overloading the database with frequent authorization requests.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Object cachedData = redisTemplate.opsForValue().get("userByUsername" + username);
        if (cachedData == null) {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            redisTemplate.opsForValue().set("userByUsername" + username, userMapper.mapUserToUserDTO(user));
            return user;
        } else {
            UserDTO dto = objectMapper.convertValue(cachedData, new TypeReference<UserDTO>() {});
            if (dto == null || dto.getPassword() == null) {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
                redisTemplate.opsForValue().set("userByUsername" + username, userMapper.mapUserToUserDTO(user));
                return user;
            }
            return userMapper.mapUserDTOToUser(dto);
        }
    }
    // Checks for the existence of a user by email.
    // This is used during registration or profile creation to avoid duplicates.
    public boolean checkIfExistsByEmail(String email){
        Object obj;
        if(redisTemplate.opsForValue().get("userByEmail"+email) == null){
            obj = userRepository.findByEmail(email).orElse(null);
            if(obj != null) redisTemplate.opsForValue().set("userByEmail"+email, userMapper.mapUserToUserDTO((User) obj));
        } else {
            obj = userMapper.mapUserDTOToUser(objectMapper.convertValue(redisTemplate.opsForValue().get("userByEmail"+email), new TypeReference<UserDTO>() {}));
        }
        return obj != null;
    }
    // Saves a new User after password encoding and role assignment.
    // Цей метод викликають контролери створення студента чи викладача перед збереженням профілю.
    public void create(User user){
        userRepository.save(user);
        redisTemplate.opsForValue().set("userByEmail"+user.getEmail(), userMapper.mapUserToUserDTO(user));
        redisTemplate.opsForValue().set("userByUsername"+user.getEmail(), userMapper.mapUserToUserDTO(user));
    }
}
