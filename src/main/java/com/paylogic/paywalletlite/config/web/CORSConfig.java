package com.paylogic.paywalletlite.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CORSConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://localhost:8080",
                "capacitor://localhost",
                "ionic://localhost",
                "http://localhost:8100"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "X-Auth-Token"
        ));

        // Spring 5.3 attend Boolean (wrapper), pas boolean primitif
        configuration.setAllowCredentials(Boolean.TRUE);

        // Spring 5.3 attend Long (wrapper), pas long primitif
        configuration.setMaxAge(Long.valueOf(3600L));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}