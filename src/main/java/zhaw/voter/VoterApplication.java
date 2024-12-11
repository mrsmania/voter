package zhaw.voter;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zhaw.voter.util.HasLogger;

import java.util.Arrays;

@SpringBootApplication
public class VoterApplication implements HasLogger {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(VoterApplication.class, args);
    }

    @PostConstruct
    public void afterInit() {
        String applicationName = env.getProperty("spring.application.name");
        String openApiInfo="";
        boolean hasDevProfile = Arrays.asList(env.getActiveProfiles()).contains("dev");
        getLogger().info("Active Profile(s): " + Arrays.toString(env.getActiveProfiles()));
        if (hasDevProfile) {
            openApiInfo = "*** YOU ARE IN DEV PROFILE ***\n";
        }
        openApiInfo += """
                     API documentation: http://localhost:8080/swagger-ui/index.html
                     Download yaml file: http://localhost:8080/v3/api-docs.yaml\s
                     """;
        System.out.println("\n\nApplication [" + applicationName + "]\n" +
        openApiInfo);
    }
}
