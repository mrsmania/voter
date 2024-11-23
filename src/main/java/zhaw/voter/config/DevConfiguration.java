package zhaw.voter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import zhaw.voter.controller.PollController;
import zhaw.voter.util.HasLogger;

@Configuration
@Profile("dev")
public class DevConfiguration implements HasLogger {

    @Autowired
    private PollController pollController;

    public DevConfiguration() {
        getLogger().info("*****YOU ARE IN DEV PROFILE*****");
    }

    @PostConstruct
    public void createPollInDevProfile() {
        String testEmail = "devuser@voter.test";

        try {
            var response = pollController.createPoll(testEmail);

            if (response.getStatusCode().is2xxSuccessful()) {
                getLogger().info("Neue Umfrage erfolgreich erstellt: " + response.getBody());
            } else {
                getLogger().info("Fehler beim Erstellen der Umfrage: " + response.getBody());
            }
        } catch (Exception e) {
            getLogger().error("Fehler beim Erstellen der Umfrage: " + e);
        }
    }
}
