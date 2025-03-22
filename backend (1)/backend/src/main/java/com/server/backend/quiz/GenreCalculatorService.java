package com.server.backend.quiz;

import com.server.backend.dto.UserAnswerDTO;
import com.server.backend.festival.Genre;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreCalculatorService {

    private final AnswerRepository answerRepository;

    public GenreCalculatorService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Genre calculateGenre(List<UserAnswerDTO> userAnswers) {
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

        // Returnăm genul cu scorul cel mai mare
        return genreScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    // Funcție simplă care mapează întrebările la genuri în funcție de text
    private Genre mapQuestionToGenre(String questionText) {
        if (questionText.contains("rock")) return Genre.ROCK;
        if (questionText.contains("electronic") || questionText.contains("edm")) return Genre.EDM;
        if (questionText.contains("pop")) return Genre.POP;
        if (questionText.contains("hip-hop") || questionText.contains("urban")) return Genre.URBAN;
        if (questionText.contains("folk")) return Genre.FOLK;
        return null;
    }
}
