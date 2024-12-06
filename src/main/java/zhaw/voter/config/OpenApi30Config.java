package zhaw.voter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class OpenApi30Config {
    private final String moduleName;
    private final String apiVersion;

    public OpenApi30Config(
            @Value("${spring.application.name}") String moduleName,
            @Value("${springdoc.version}") String apiVersion) {
        this.moduleName = moduleName;
        this.apiVersion = apiVersion;
    }

    @Profile("dev")
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.server}") String contextPath) {
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
        return new OpenAPI()
                .info(new Info().title(apiTitle).version(apiVersion));
    }
}
