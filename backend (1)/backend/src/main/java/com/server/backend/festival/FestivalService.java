package com.server.backend.festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FestivalService {

    @Autowired
    private FestivalRepository festivalRepository;

    // Creare Festival
    public Festival createFestival(Festival festival) {
        return festivalRepository.save(festival);
    }

    // Citire toate Festivalurile
    public List<Festival> getAllFestivals() {
        return festivalRepository.findAll();
    }

    // Citire un Festival după id
    public Optional<Festival> getFestivalById(Integer id) {
        return festivalRepository.findById(id);
    }

    // Actualizare Festival
    public Festival updateFestival(Integer id, Festival festivalDetails) {
        if (festivalRepository.existsById(id)) {
            festivalDetails.setId(id);  // Setăm id-ul pentru a face update
            return festivalRepository.save(festivalDetails);
        }
        return null; // Poți arunca o excepție în loc, dacă dorești
    }

    // Ștergere Festival
    public void deleteFestival(Integer id) {
        festivalRepository.deleteById(id);
    }
}