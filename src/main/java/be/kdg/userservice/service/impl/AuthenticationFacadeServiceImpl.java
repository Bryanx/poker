package be.kdg.userservice.service.impl;

import be.kdg.userservice.service.api.AuthenticationFacadeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
