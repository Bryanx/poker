package be.kdg.userservice.security.config;

import be.kdg.userservice.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class JWTConfig {
    private final ClientDetailsService clientDetailsService;
    private final UserServiceImpl userService;

    @Autowired
    public JWTConfig(ClientDetailsService clientDetailsService, UserServiceImpl userService) {
        this.clientDetailsService = clientDetailsService;
        this.userService = userService;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List tokenEnhancerList = new ArrayList();
        tokenEnhancerList.add(new JWTTokenEnhancerConfig());
        tokenEnhancerList.add(tokenEnhancer());

        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
        return tokenEnhancerChain;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter());

        jwtAccessTokenConverter.setSigningKey("123");

        return jwtAccessTokenConverter;
    }

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();

        DefaultUserAuthenticationConverter userConverter = new DefaultUserAuthenticationConverter();
        userConverter.setUserDetailsService(userService);

        converter.setUserTokenConverter(userConverter);

        return converter;
    }

    @Bean
    public AuthorizationServerTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
