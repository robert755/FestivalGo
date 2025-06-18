package com.server.backend.participare;

import com.server.backend.festival.Festival;
import com.server.backend.festival.FestivalRepository;
import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        // 1. Luăm toate participările active (status = PARTICIPA) pentru utilizatorul dat
        List<Participare> toateParticiparile = participareRepository.findParticipariByUserId(userId);

        // 2. Creăm o listă nouă unde vom pune doar cele valabile (endDate > azi)
        List<Participare> doarViitoare = new ArrayList<>();

        // 3. Luăm data curentă (azi, fără ora)
        LocalDate azi = LocalDate.now();

        // 4. Verificăm fiecare participare
        for (Participare p : toateParticiparile) {
            // Luăm data de sfârșit a festivalului
            LocalDate dataSfarsit = p.getFestival().getEndDate();

            // Dacă festivalul se termină după azi, îl adăugăm în listă
            if (dataSfarsit.isAfter(azi)) {
                doarViitoare.add(p);
            }
        }

        // 5. Returnăm doar festivalurile viitoare
        return doarViitoare;
    }


    @Transactional
    public Participare adaugaParticipare(Integer userId, Integer festivalId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Userul nu a fost găsit"));

        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new RuntimeException("Festivalul nu a fost găsit"));

        // Verifică dacă deja există o participare activă (PARTICIPA)
        boolean existaDeja = participareRepository.existsByUserIdAndFestivalIdAndStatus(userId, festivalId, Status.PARTICIPA);
        if (existaDeja) {
            throw new RuntimeException("Ești deja înscris la acest festival.");
        }
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
    @Transactional
    public void confirmaBiletCumparat(Integer userId, Integer festivalId) {
        User user = userRepository.findById(userId).orElseThrow();
        Festival festival = festivalRepository.findById(festivalId).orElseThrow();

        List<Participare> existente = participareRepository.findByUserIdAndFestivalId(userId, festivalId);

        // Caută o participare care nu este ANULATĂ
        Participare participareValida = existente.stream()
                .filter(p -> p.getStatus() != Status.ANULAT)
                .findFirst()
                .orElse(null);

        if (participareValida != null) {
            participareValida.setStatus(Status.CUMPARAT);
            participareRepository.save(participareValida);
        } else {
            // Dacă toate sunt ANULATE sau nu există deloc, creează una nouă
            Participare noua = new Participare();
            noua.setUser(user);
            noua.setFestival(festival);
            noua.setStatus(Status.CUMPARAT);
            participareRepository.save(noua);
        }
    }


}
