package com.test.reminder.service.impl;

import com.test.reminder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        switch (principal) {
            case OidcUser oidcUser -> {
                String subId = oidcUser.getName();
                return userRepository.findBySubId(subId).getId();
            }
            case Jwt jwt -> {
                String subId = jwt.getClaim("sub");
                return userRepository.findBySubId(subId).getId();
            }
            case OAuth2User oAuth2User -> {
                String subId = oAuth2User.getName();
                return userRepository.findBySubId(subId).getId();
            }
            case null, default -> throw new AccessDeniedException("User is not authorized");
        }
    }

    public void checkOwnership(Long userOwnerId) {
        if (!Objects.equals(userOwnerId, getCurrentUserId())) {
            throw new AccessDeniedException("User is not authorized");
        }
    }
}
