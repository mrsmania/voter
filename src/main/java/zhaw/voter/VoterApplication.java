package zhaw.voter;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zhaw.voter.util.HasLogger;

import java.net.InetAddress;
import java.util.Arrays;

@SpringBootApplication
public class VoterApplication implements HasLogger {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        try {
            String currentIp = InetAddress.getLocalHost().getHostAddress();
            System.setProperty("NETWORK_IP", currentIp); // Set network IP to access app from other devices in the same network - only works in DEV profile since the  NETWORK_IP variable is only used in the application-dev.properties file and not in the application-prod.properties.
        } catch (Exception e) {
            e.printStackTrace();
        }
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
