package com.server.backend.quiz;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/questions")
    public Question addQuestion(@RequestBody Question question) {
        return quizService.createQuestion(question);
    }


    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return quizService.getAllQuestions();
    }

    @DeleteMapping("/questions/{id}")
    public void deleteQuestion(@PathVariable Integer id) {
        quizService.deleteQuestion(id);
    }




}
