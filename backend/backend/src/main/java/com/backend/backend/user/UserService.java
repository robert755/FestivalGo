package com.backend.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        String storedPassword = userRepository.findPasswordByUsername(username);

        if (storedPassword == null) {
            return false;  // Dacă userul nu există
        }

        return storedPassword.equals(password);  // Comparare directă
    }

    public User create (User myUser){
        return userRepository.save(myUser);
    }
}
