package com.example.demo.services.security;

import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserService contains business operations for the "user accounts" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Searches for a User by email, which Spring Security receives from the login form.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
    // Checks for the existence of a user by email.
    // This is used during registration or profile creation to avoid duplicates.
    public boolean checkIfExistsByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    // Saves a new User after password encoding and role assignment.
    // Цей метод викликають контролери створення студента чи викладача перед збереженням профілю.
    public void create(User user){
        userRepository.save(user);
    }
}
