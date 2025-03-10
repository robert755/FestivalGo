package com.server.backend.user;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

   @Transactional
    public User createUser(User user) {
        // Criptăm parola înainte de a o salva
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Creăm un nou obiect User și setăm manual fiecare câmp
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodedPassword);  // Parola criptată
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(user.getRole());

        // Salvăm utilizatorul în baza de date
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        // Verifică dacă utilizatorul există și dacă parola introdusă corespunde cu cea criptată
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

}
