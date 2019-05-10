package be.kdg.gameservice.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Configuration class that configures the resource server, it will handle authentication based on the
 * JWT that is send with every request to a rest endpoint.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final JWTConfig jwtConfig;

    public ResourceServerConfig(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Configure the resource server to use our own token service.* @param config
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(jwtConfig.resourceTokenServices());
    }

    /**
     * Configure every request to a rest endpoint to be authenticated.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }
}
