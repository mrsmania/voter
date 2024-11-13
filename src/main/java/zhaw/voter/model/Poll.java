package zhaw.voter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String password = UUID.randomUUID().toString().substring(0, 6).toUpperCase();


    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();

    private String hostUserEmail;
    private boolean active = false;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean getActive() { // Use getActive() instead of isActive()
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
            question.setPoll(this); // Set the poll reference in each question
        }
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
}