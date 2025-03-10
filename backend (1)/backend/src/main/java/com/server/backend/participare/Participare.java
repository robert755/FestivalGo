package com.server.backend.participare;

import com.server.backend.festival.Festival;
import com.server.backend.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Participare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Enumerated(EnumType.STRING) // Salvăm status-ul ca text în baza de date
    @Column(nullable = false)
    private Status status;

    private LocalDateTime dataInscriere;

    public Participare() {
        this.dataInscriere = LocalDateTime.now();
    }

    public Participare(User user, Festival festival, Status status) {
        this.user = user;
        this.festival = festival;
        this.status = status;
        this.dataInscriere = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataInscriere() {
        return dataInscriere;
    }

    @PrePersist
    protected void onCreate() {
        this.dataInscriere = LocalDateTime.now();
    }

}
