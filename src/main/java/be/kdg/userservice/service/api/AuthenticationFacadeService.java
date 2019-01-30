package be.kdg.userservice.service.api;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeService {
    Authentication getAuthentication();
}
