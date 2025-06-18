package com.server.backend.user;

import com.server.backend.festival.Genre;
import com.server.backend.participare.Participare;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Username-ul nu poate fi gol")
    @Size(min = 3, max = 20, message = "Username-ul trebuie să aibă între 3 și 20 de caractere")
    private String username;
    @NotBlank(message = "Email-ul nu poate fi gol")
    @Email(message = "Email invalid")
    private String email;
    @NotBlank(message = "Parola nu poate fi goală")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere")
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Genre preferredGenre;

    // Constructori
    public User() {}
    public User(String username, String email, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters și Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public Genre getPreferredGenre() {
        return preferredGenre;
    }

    public void setPreferredGenre(Genre preferredGenre) {
        this.preferredGenre = preferredGenre;
    }

    @OneToMany(mappedBy = "user")
    private List<Participare> participari;
}
