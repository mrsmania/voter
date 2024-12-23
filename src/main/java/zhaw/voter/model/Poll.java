package zhaw.voter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String password;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();

    private String hostUserEmail;
    private boolean active = false;

    public Poll() {
    }

    public Poll(String hostUserEmail, boolean active, String token, String password) {
        this.hostUserEmail = hostUserEmail;
        this.active = active;
        this.token = token;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getActive() {
        return active;
    }

    public String getHostUserEmail() {
        return hostUserEmail;
    }

    public void setHostUserEmail(String hostUsername) {
        this.hostUserEmail = hostUsername;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        for (Question question : questions) {
            question.setPoll(this);
        }
    }

    public void setQuestion(Question question) {
        this.questions.add(question);
        question.setPoll(this);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setPoll(this);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setPoll(null);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}