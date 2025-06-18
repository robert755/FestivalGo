package com.server.backend.user;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRateLimiterService rateLimiterService;


    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user) {
        // Criptare
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getEmail().toLowerCase().endsWith("@festivalgo.com")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        userService.createUser(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, org.springframework.validation.BindingResult result, HttpServletRequest servletRequest) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        String clientIp = servletRequest.getRemoteAddr();

        if (!rateLimiterService.isAllowed(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Prea multe încercări. Încearcă din nou peste un minut.");
        }

        User user = userService.findByUsername(request.getUsername());

        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autentificare eșuată!");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("preferredGenre", user.getPreferredGenre());
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public List<User> getAllUsers() {
        List<User> totiUserii = userRepository.findAll();
        List<User> doarUseri = new ArrayList<>();
        for (User user : totiUserii) {
            if (user.getRole() == Role.USER) {
                doarUseri.add(user);
            }
        }
        return doarUseri;
    }

}

