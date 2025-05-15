package com.server.backend.festival;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FestivalService {

    @Autowired
    private FestivalRepository festivalRepository;

    @Transactional
    public Festival createFestival(Festival festival) {
        // Creăm un nou obiect Festival și setăm valorile
        Festival newFestival = new Festival();

        // Folosim 'this' pentru a seta valorile câmpurilor din obiectul 'newFestival'
        newFestival.setName(festival.getName());
        newFestival.setLocation(festival.getLocation());
        newFestival.setDescription(festival.getDescription());
        newFestival.setStartDate(festival.getStartDate());
        newFestival.setEndDate(festival.getEndDate());
        newFestival.setImagePath(festival.getImagePath());
        newFestival.setGenre(festival.getGenre());
        // Salvează obiectul 'newFestival' în baza de date
        return festivalRepository.save(newFestival);
    }
    public String saveImage(MultipartFile imageFile) throws IOException {
        String folder = "uploads/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(folder + fileName);
        Files.createDirectories(imagePath.getParent());
        System.out.println("Imagine salvată în: " + imagePath.toAbsolutePath()); // verificare
        Files.write(imagePath, imageFile.getBytes());
        return fileName;
    }

    @Transactional
    public List<Festival> getAllFestivals() {
        return festivalRepository.findAll();
    }

    // Citire un Festival după id
    public Optional<Festival> getFestivalById(Integer id) {
        return festivalRepository.findById(id);
    }


    @Transactional
    public Festival updateFestival(Integer id, Festival festivalDetails) {
        Optional<Festival> optionalFestival = festivalRepository.findById(id);

        if (optionalFestival.isPresent()) {
            Festival festival = optionalFestival.get();

            festival.setName(festivalDetails.getName());
            festival.setDescription(festivalDetails.getDescription());
            festival.setStartDate(festivalDetails.getStartDate());
            festival.setEndDate(festivalDetails.getEndDate());
            festival.setGenre(festivalDetails.getGenre());
            festival.setLocation(festivalDetails.getLocation());

            // ✅ doar dacă ai primit o imagine nouă
            if (festivalDetails.getImagePath() != null) {
                festival.setImagePath(festivalDetails.getImagePath());
            }

            return festivalRepository.save(festival);
        }

        return null;
    }


    @Transactional
    public void deleteFestival(Integer id) {
        festivalRepository.deleteById(id);
    }
}