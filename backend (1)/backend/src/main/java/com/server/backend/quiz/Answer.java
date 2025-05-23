package com.server.backend.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String answerText;  // Textul răspunsului
    private Integer score;      // Scorul asociat răspunsului

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;  // Legătura cu întrebarea

    public Answer() {
    }


    public Answer(String answerText, Integer score) {
        this.answerText = answerText;
        this.score = score;
    }

    // Getters și Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
