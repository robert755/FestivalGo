package com.server.backend.quiz;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuizService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public Question createQuestion(Question question) {
        // Setăm fiecare răspuns să știe la ce întrebare aparține
        for (Answer answer : question.getAnswers()) {
            answer.setQuestion(question);  // 🔥 Asta e cheia!
        }
        return questionRepository.save(question);
    }

    @Transactional
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Answer> getAnswersForQuestion(Integer questionId) {
        return answerRepository.findAll()
                .stream()
                .filter(a -> a.getQuestion().getId().equals(questionId))
                .toList();
    }
}
