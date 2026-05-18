package com.paylogic.paywalletlite.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AuthenticationContext {

    /**
     * Extracts userId from current authentication context.
     * Temporary implementation - returns mock UUID for development.
     * Replace with JWT token parsing when auth module is ready.
     */
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            // When JWT is ready: extract from JWT claims
            // For now: return principal as UUID string
            String principal = authentication.getName();
            try {
                return UUID.fromString(principal);
            } catch (IllegalArgumentException e) {
                // Mock user for development testing
                return UUID.fromString("00000000-0000-0000-0000-000000000001");
            }
        }

        // Development fallback - remove in production
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }
}