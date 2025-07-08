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

    // Mapare cuvânt cheie → gen muzical
    private final Map<Genre, List<String>> keywordMap = Map.of(
            Genre.ROCK, List.of("expresivă","propriul drum", "intens"),
            Genre.EDM, List.of("energie","alert", "spontaneitate"),
            Genre.POP, List.of("popular", "grup", "armonia"),
            Genre.URBAN, List.of("încrezător", "competiția", "stilul personal"),
            Genre.FOLK, List.of("autentice", "natură", "profunzimea"),
            Genre.JAZZ, List.of("eleganța", "flexibilitatea", "competitivă"),
            Genre.INDIE, List.of("creativ", "neobișnuite", "grupuri mici")
    );

    public GenreCalculatorService(AnswerRepository answerRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public Genre calculateAndSaveGenre(Integer userId, List<UserAnswerDTO> userAnswers) {
        // 1️⃣ Inițializăm scorurile
        Map<Genre, Integer> genreScores = new HashMap<>();
        for (Genre genre : Genre.values()) {
            genreScores.put(genre, 0);
        }

        // Procesam fiecare raspuns
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

        // Găsim scorul maxim
        int maxScore = genreScores.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        // Colectăm toate genurile cu scorul maxim
        List<Genre> topGenres = genreScores.entrySet().stream()
                .filter(entry -> entry.getValue() == maxScore)
                .map(Map.Entry::getKey)
                .toList();

        // Alegem random un gen din cele cu scorul maxim
        Random random = new Random();
        Genre calculatedGenre = topGenres.get(random.nextInt(topGenres.size()));

        // Salvam genul in profilul utilizatorului
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPreferredGenre(calculatedGenre);
            userRepository.save(user);
        }

        return calculatedGenre;
    }

    // caută cuvinte cheie din întrebări
    private Genre mapQuestionToGenre(String questionText) {
        String lowerText = questionText.toLowerCase();
        for (Map.Entry<Genre, List<String>> entry : keywordMap.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowerText.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
