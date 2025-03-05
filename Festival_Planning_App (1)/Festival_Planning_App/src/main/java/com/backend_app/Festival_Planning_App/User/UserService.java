package com.backend_app.Festival_Planning_App.User;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    // Înregistrare utilizator
    public User createUser(User user) {
        // Criptarea parolei
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // criptăm parola
        user.setRole(Role.USER);  // Setăm rolul de utilizator
        return userRepository.save(user);
    }

    // Găsim utilizatorul după username
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);  // Căutăm utilizatorul după username
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);  // Aruncăm excepție dacă nu-l găsim
        }
        return user;  // Returnăm utilizatorul
    }


}
