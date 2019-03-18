package be.kdg.gameservice.shared.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

/**
 * A simple configuration class that is used to create a bean of the modelmapper.
 */
@Configuration
@Getter
public class WebConfig {
    @Value("${userservice.url}")
    private String userServiceUrl;
    @Value("${token.url}")
    private String TOKEN_URL;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
