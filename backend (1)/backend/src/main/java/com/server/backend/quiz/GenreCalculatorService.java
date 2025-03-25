package com.server.backend.quiz;

import com.server.backend.dto.UserAnswerDTO;
import com.server.backend.festival.Genre;
import com.server.backend.user.User;
import com.server.backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreCalculatorService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public GenreCalculatorService(AnswerRepository answerRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    // Acum primim și userId
    public Genre calculateAndSaveGenre(Integer userId, List<UserAnswerDTO> userAnswers) {
        // Inițializăm scorul pentru fiecare gen
        Map<Genre, Integer> genreScores = new HashMap<>();
        for (Genre genre : Genre.values()) {
            genreScores.put(genre, 0);
        }

        // Parcurgem fiecare răspuns trimis de utilizator
        for (UserAnswerDTO dto : userAnswers) {
            Optional<Answer> optionalAnswer = answerRepository.findById(dto.getAnswerId());
            if (optionalAnswer.isEmpty()) continue;

            Answer answer = optionalAnswer.get();
            String questionText = answer.getQuestion().getQuestionText().toLowerCase();
            Genre genre = mapQuestionToGenre(questionText);

            if (genre != null) {
                int currentScore = genreScores.get(genre);
                genreScores.put(genre, currentScore + answer.getScore());
            }
        }

        // Calculăm genul cu cel mai mare scor
        Genre calculatedGenre = genreScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Salvăm genul în User
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPreferredGenre(calculatedGenre); // salvăm genul
            userRepository.save(user);               // persistăm în DB
        }

        return calculatedGenre;
    }

    // Maparea întrebărilor la genuri
    private Genre mapQuestionToGenre(String questionText) {
        if (questionText.contains("rock")) return Genre.ROCK;
        if (questionText.contains("electronică") || questionText.contains("edm")) return Genre.EDM;
        if (questionText.contains("pop") || questionText.contains("radio")) return Genre.POP;
        if (questionText.contains("hip-hop") || questionText.contains("urban")) return Genre.URBAN;
        if (questionText.contains("folk") || questionText.contains("chitară")) return Genre.FOLK;
        return null;
    }
}
