package be.kdg.userservice.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public abstract class BaseController {
    private static final String ID_KEY = "uuid";
    @Autowired
    private ResourceServerTokenServices resourceTokenServices;

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    protected String getUserId(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue())
                .getAdditionalInformation().get(ID_KEY).toString();
    }
}
