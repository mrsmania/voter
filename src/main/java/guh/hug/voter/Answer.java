package guh.hug.voter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String text;


    public void setId(Long id) {
        this.Id = id;
    }

    public Long getId() {
        return Id;
    }
}
