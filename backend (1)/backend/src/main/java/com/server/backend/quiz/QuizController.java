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

    @PostMapping("/answers")
    public Answer addAnswer(@RequestBody Answer answer) {
        return quizService.createAnswer(answer);
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return quizService.getAllQuestions();
    }

    @GetMapping("/questions/{id}/answers")
    public List<Answer> getAnswers(@PathVariable Integer id) {
        return quizService.getAnswersForQuestion(id);
    }
}
