package be.kdg.gameservice.shared.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A simple configuration class that is used to create a bean of the modelmapper.
 */
@Configuration
@Getter
public class WebConfig {
    @Value("userservice.url")
    private String userServiceUrl;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
