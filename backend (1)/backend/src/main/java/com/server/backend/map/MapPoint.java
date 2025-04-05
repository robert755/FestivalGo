package com.server.backend.map;

import com.server.backend.festival.Festival;
import jakarta.persistence.*;

@Entity
public class MapPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double latitude;
    private double longitude;

    private String label;

    @Column(length = 10)
    private String emoji;

    @ManyToOne
    @JoinColumn(name = "festival_id")
    private Festival festival;

    public MapPoint() {}

    public MapPoint(double latitude, double longitude, String label, String emoji, Festival festival) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.emoji = emoji;
        this.festival = festival;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }
}
