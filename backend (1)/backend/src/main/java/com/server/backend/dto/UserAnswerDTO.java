package com.server.backend.dto;

/**
 * DTO simplu pentru a primi ID-ul unui răspuns selectat de utilizator.
 */
public class UserAnswerDTO {
    private Integer answerId;

    public UserAnswerDTO() {
    }

    public UserAnswerDTO(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }
}
