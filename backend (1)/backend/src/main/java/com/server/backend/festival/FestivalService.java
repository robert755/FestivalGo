package com.server.backend.festival;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        // Salvează obiectul 'newFestival' în baza de date
        return festivalRepository.save(newFestival);
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
        if (festivalRepository.existsById(id)) {
            festivalDetails.setId(id);  // Setăm id-ul pentru a face update
            return festivalRepository.save(festivalDetails);
        }
        return null; // Poți arunca o excepție în loc, dacă dorești
    }

    @Transactional
    public void deleteFestival(Integer id) {
        festivalRepository.deleteById(id);
    }
}