package com.server.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) { // Primi un obiect User complet
        return userRepository.save(user);
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        // Verifică dacă utilizatorul există și dacă parola introdusă corespunde cu cea criptată
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
}
