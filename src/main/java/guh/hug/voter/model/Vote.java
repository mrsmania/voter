package guh.hug.voter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "option_id")
    @JsonIgnoreProperties("votes")
    private Option option;
    // Constructors, getters, and setters
    public Vote() {}

    public Vote(String username, Option option) {
        this.username = username;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
