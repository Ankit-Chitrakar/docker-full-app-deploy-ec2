package com.ankit.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Allow credentials (required for Authorization headers, bearer tokens, etc.)
        config.setAllowCredentials(true);

        // 2. Explicitly define allowed origins (Local dev, Docker network, and your live EC2 IP/Domain)
        config.setAllowedOrigins(List.of(
                "http://localhost:5173", // Vite local development server
                "http://localhost:80",   // Local Docker Nginx container
                "http://localhost",      // Local root host
                "http://<YOUR_EC2_PUBLIC_IP>" // Actual live AWS EC2 Public IP or Domain
        ));

        // 3. Allow all standard headers
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type"
        ));

        // 4. Allow all standard HTTP methods used by your app
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}