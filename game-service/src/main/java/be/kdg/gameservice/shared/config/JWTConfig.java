package be.kdg.gameservice.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for creating beans that are required for security with JWT.
 */
@Configuration
public class JWTConfig {
    /**
     * Creates a JwtTokenStore with a modified TokenEnhancer.
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    /**
     * A composite token enhancer that loops over its delegate enhancers. In our case it
     * consists out of the default JwtAccessTokenEnhancer and our own JWTTokenEnhancerConfig.
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List tokenEnhancerList = new ArrayList();
        tokenEnhancerList.add(tokenEnhancer());

        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
        return tokenEnhancerChain;
    }

    /**
     * Helper that translates between JWT encoded token values and OAuth authentication
     * information (in both directions). Also acts as a TokenEnhancer when tokens are
     * granted. We also set the key for symmetric encryption.
     */
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter());

        jwtAccessTokenConverter.setSigningKey("123");

        return jwtAccessTokenConverter;
    }

    /**
     * Converter implementation for a token service that stores authentication data inside the token. We add
     * our own userService which will be used for credential verification.
     */
    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();

        DefaultUserAuthenticationConverter userConverter = new DefaultUserAuthenticationConverter();

        converter.setUserTokenConverter(userConverter);

        return converter;
    }

    /**
     * DefaultTokenService for authentication that we customize with our own tokenStore,
     * tokenEnhancerChain and support for refreshToken.
     */
    @Bean
    public ResourceServerTokenServices resourceTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
