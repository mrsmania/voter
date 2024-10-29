package guh.hug.voter;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String text;

    @ElementCollection
    private List<String> answers;

    public Question(String text , List<String> answers) {
        this.text = text;
        this.answers = answers;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Long getId() {
        return Id;
    }
}
