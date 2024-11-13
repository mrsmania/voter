package zhaw.voter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();


    private Boolean multipleChoice;

    @ManyToOne
    @JoinColumn(name = "POLL_ID")
    @JsonBackReference
    private Poll poll;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
        for (Option option : options) {
            option.setQuestion(this); // Set the poll reference in each question
        }
    }

    public void addOption(Option option) {
        this.options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(Option option) {
        this.options.remove(option);
        option.setQuestion(null);
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }


    public Boolean getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(Boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }
}
