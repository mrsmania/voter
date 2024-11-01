package guh.hug.voter.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token = UUID.randomUUID().toString().substring(0, 6); //TODO: ensure uniqueness

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    private String hostUsername;
    private boolean isActive = false;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}