package com.backend_app.Festival_Planning_App.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Logare utilizator
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);  // Căutăm utilizatorul
        // Verificăm dacă parola introdusă se potrivește cu cea criptată
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid username or password";  // Dacă nu se potrivește
        } else {
            return "Login successful";  // Dacă parola se potrivește
        }
    }
}
