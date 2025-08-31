package com.aust.its.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    private final List<AuthenticationProvider> authenticationProviders;

    public CustomAuthenticationManager(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        throw new ProviderNotFoundException("Authentication provider not found");
    }
}