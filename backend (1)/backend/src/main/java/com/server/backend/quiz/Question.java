package com.server.backend.quiz;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String questionText;  // Textul întrebării

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;  // Lista de răspunsuri asociate întrebării

    // Constructor fără parametri
    public Question() {
    }

    // Constructor cu parametri
    public Question(String questionText) {
        this.questionText = questionText;
    }

    // Getters și Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}