package com.server.backend.participare;

import com.server.backend.festival.Festival;
import com.server.backend.festival.FestivalRepository;
import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipareService {
    @Autowired
    private ParticipareRepository participareRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FestivalRepository festivalRepository;
    public List<Participare> getAllParticipari() {
        return participareRepository.findAll();
    }
    public List<Festival> getFestivaluriByUser(Integer userId) {
        return participareRepository.findFestivaluriByUserId(userId);
    }

    @Transactional
    public Participare adaugaParticipare(Integer userId, Integer festivalId) {
        // Căutăm utilizatorul în baza de date
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Userul nu a fost găsit"));

        // Căutăm festivalul în baza de date
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new RuntimeException("Festivalul nu a fost găsit"));

        // Creăm un nou obiect Participare
        Participare participare = new Participare();

        // Setăm valorile câmpurilor din obiectul 'participare' folosind 'this'
        participare.setUser(user);
        participare.setFestival(festival);
        participare.setStatus(Status.PARTICIPA);  // Statusul implicit va fi 'PARTICIPA'

        // Salvăm participarea în baza de date
        return participareRepository.save(participare);
    }

    @Transactional
    public Participare actualizeazaStatusParticipare(Integer id) {
        Participare participare = participareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participarea nu a fost găsită"));

        // Când actualizăm participarea, statusul devine automat ANULAT
        participare.setStatus(Status.ANULAT);
        return participareRepository.save(participare);
    }


}
