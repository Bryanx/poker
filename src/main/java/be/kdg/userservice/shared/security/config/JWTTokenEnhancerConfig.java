package be.kdg.userservice.shared.security.config;

import be.kdg.userservice.shared.security.model.CustomUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class that enables us to change to content of JWT.
 */
@Configuration
public class JWTTokenEnhancerConfig implements TokenEnhancer {
    /**
     * Adds our custom claims to the JWT, it adds our id, username and role to the payload.
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String roles = "";
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) customUserDetails.getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            roles = roles.concat(" " + grantedAuthority.getAuthority());
        }
        roles = roles.trim();

        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("uuid", customUserDetails.getId());
        additionalInfo.put("username", customUserDetails.getUsername());
        additionalInfo.put("role", roles);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
