package com.server.backend.festival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {

    @Autowired
    private FestivalService festivalService;

    // POST - Creare Festival
    @PostMapping
    public ResponseEntity<Festival> createFestival(@RequestBody Festival festival) {
        Festival createdFestival = festivalService.createFestival(festival);
        return new ResponseEntity<>(createdFestival, HttpStatus.CREATED);
    }

    // GET - Obține toate Festivalurile
    @GetMapping
    public List<Festival> getAllFestivals() {
        return festivalService.getAllFestivals();
    }

    // GET - Obține un Festival după ID
    @GetMapping("/{id}")
    public ResponseEntity<Festival> getFestivalById(@PathVariable Integer id) {
        Optional<Festival> festival = festivalService.getFestivalById(id);
        if (festival.isPresent()) {
            return new ResponseEntity<>(festival.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT - Actualizează un Festival
    @PutMapping("/{id}")
    public ResponseEntity<Festival> updateFestival(@PathVariable Integer id, @RequestBody Festival festivalDetails) {
        Festival updatedFestival = festivalService.updateFestival(id, festivalDetails);
        if (updatedFestival != null) {
            return new ResponseEntity<>(updatedFestival, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Șterge un Festival
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFestival(@PathVariable Integer id) {
        festivalService.deleteFestival(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}