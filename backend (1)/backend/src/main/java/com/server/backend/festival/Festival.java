package com.server.backend.festival;

import com.server.backend.participare.Participare;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String location;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private String imagePath;
    @Enumerated(EnumType.STRING)
      private Genre  genre;
    // Constructor fără parametri
    public Festival() {
    }

    // Constructor cu parametri
    public Festival(String name, String location, String description, LocalDate startDate, LocalDate endDate,Genre genre,String ImagePath) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.genre= genre;
        this.imagePath=imagePath;
    }

    // Getters și Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @OneToMany(mappedBy = "festival")
    private List<Participare> participari;
}
