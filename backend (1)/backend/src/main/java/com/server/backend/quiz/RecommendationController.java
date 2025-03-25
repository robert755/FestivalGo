package com.server.backend.quiz;

import com.server.backend.dto.UserAnswerDTO;
import com.server.backend.festival.Genre;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final GenreCalculatorService genreCalculatorService;

    public RecommendationController(GenreCalculatorService genreCalculatorService) {
        this.genreCalculatorService = genreCalculatorService;
    }

    @PostMapping("/calculate/{userId}")
    public Genre calculateAndSave(@PathVariable Integer userId,
                                  @RequestBody List<UserAnswerDTO> answers) {
        return genreCalculatorService.calculateAndSaveGenre(userId, answers);
    }

}
