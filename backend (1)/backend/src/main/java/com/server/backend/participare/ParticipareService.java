package com.server.backend.participare;

import com.server.backend.festival.Festival;
import com.server.backend.festival.FestivalRepository;
import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Participare> getParticipariByUser(Integer userId) {
        return participareRepository.findParticipariByUserId(userId);
    }

    @Transactional
    public Participare adaugaParticipare(Integer userId, Integer festivalId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Userul nu a fost găsit"));

        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new RuntimeException("Festivalul nu a fost găsit"));

        Participare participare = new Participare();
        participare.setUser(user);
        participare.setFestival(festival);
        participare.setStatus(Status.PARTICIPA);

        return participareRepository.save(participare);
    }

    @Transactional
    public Participare actualizeazaStatusParticipare(Integer id) {
        Participare participare = participareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participarea nu a fost găsită"));

        participare.setStatus(Status.ANULAT);
        return participareRepository.save(participare);
    }
}
