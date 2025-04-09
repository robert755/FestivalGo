package com.server.backend.participare;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participari")
public class ParticipareController {

    private final ParticipareService participareService;

    public ParticipareController(ParticipareService participareService) {
        this.participareService = participareService;
    }

    @GetMapping
    public List<Participare> getAllParticipari() {
        return participareService.getAllParticipari();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Participare>> getParticipariByUser(@PathVariable Integer userId) {
        List<Participare> participari = participareService.getParticipariByUser(userId);
        return ResponseEntity.ok(participari);
    }

    @PostMapping("/adauga")
    public ResponseEntity<Participare> adaugaParticipare(@RequestParam Integer userId, @RequestParam Integer festivalId) {
        Participare participare = participareService.adaugaParticipare(userId, festivalId);
        return ResponseEntity.ok(participare);
    }

    @PutMapping("/anuleaza/{id}")
    public ResponseEntity<Participare> anuleazaParticipare(@PathVariable Integer id) {
        Participare participare = participareService.actualizeazaStatusParticipare(id);
        return ResponseEntity.ok(participare);
    }
}
