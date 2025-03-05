package com.backend_app.Festival_Planning_App.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        // Găsești utilizatorul după username
        User user = userRepository.findByUsername(username);

        // Dacă utilizatorul există, compari parola criptată cu parola introdusă
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;  // Parola se potrivește
        }

        return false;  // Parola nu se potrivește sau utilizatorul nu există
    }
}
