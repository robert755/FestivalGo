package com.server.backend.festival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/festivals")
public class FestivalController {

    @Autowired
    private FestivalService festivalService;

    @PostMapping(value = "/post", consumes = {"multipart/form-data"})
    public ResponseEntity<Festival> createFestivalWithImage(
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("genre") Genre genre,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            String imagePath = festivalService.saveImage(imageFile);
            Festival newFestival = new Festival(name, location, description,
                    LocalDate.parse(startDate), LocalDate.parse(endDate), genre, imagePath);
            return new ResponseEntity<>(festivalService.createFestival(newFestival), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Festival> updateFestival(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("genre") Genre genre,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            String imagePath = null;
            // dacă a fost selectată o imagine nouă, o salvăm
            if (imageFile != null && !imageFile.isEmpty()) {
                imagePath = festivalService.saveImage(imageFile);
            }
            // construim un obiect Festival cu valorile actualizate
            Festival updatedFestival = new Festival(
                    name,
                    location,
                    description,
                    LocalDate.parse(startDate),
                    LocalDate.parse(endDate),
                    genre,
                    imagePath // poate fi null → se tratează în service
            );

            Festival result = festivalService.updateFestival(id, updatedFestival);

            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // DELETE - Șterge un Festival
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFestival(@PathVariable Integer id) {
        festivalService.deleteFestival(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}