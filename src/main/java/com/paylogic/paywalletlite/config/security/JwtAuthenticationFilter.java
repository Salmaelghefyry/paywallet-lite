package com.paylogic.paywalletlite.config.security;

import com.paylogic.paywalletlite.security.crypto.HashUtil;
import com.paylogic.paywalletlite.repository.identity.DeviceSessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final DeviceSessionRepository sessionRepository;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   DeviceSessionRepository sessionRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verify session is still active in DB
        String tokenHash = HashUtil.sha256(token);
        var sessionOpt = sessionRepository.findByTokenHash(tokenHash);
        if (sessionOpt.isEmpty() || sessionOpt.get().getStatus().name().equals("REVOKED")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.emptyList()
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}