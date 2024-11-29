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
        getLogger().info("Active Profiles: " + Arrays.toString(env.getActiveProfiles()));
        if (hasDevProfile) {
            openApiInfo = """
                     http://localhost:8080/v3/api-docs
                     http://localhost:8080/v3/api-docs.yaml -> yaml file is downloaded ->
                    https://editor.swagger.io/
                     http://localhost:8080/swagger-ui.html\s
                    """;
        }
        System.out.println("\n\nApplication [" + applicationName + "] - Enter in Browser:\nhttp://localhost:8080 \n" +
        openApiInfo + "\n" +
                "Active Profiles: " + Arrays.toString(env.getActiveProfiles()) + "\n\n");
    }
}
