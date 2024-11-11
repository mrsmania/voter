package zhaw.voter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @ManyToOne
    @JsonIgnoreProperties("votes")
    @JoinColumn(name = "OPTION_ID")
    private Option option;

    public Vote() {}

    public Vote(String userEmail, Option option) {
        this.userEmail = userEmail;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String username) {
        this.userEmail = username;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
