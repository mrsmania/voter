package zhaw.voter;

import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class
VoterApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoterApplication.class, args);
    }
}
