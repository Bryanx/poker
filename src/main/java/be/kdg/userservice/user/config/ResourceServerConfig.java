package be.kdg.userservice.user.config;

import be.kdg.userservice.config.JWTConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final JWTConfig jwtConfig;

    @Autowired
    public ResourceServerConfig(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(jwtConfig.resourceTokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }
}
