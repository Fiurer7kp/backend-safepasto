package com.safepasto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-origin-patterns:}")
    private String[] allowedOriginPatterns;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var registration = registry.addMapping("/api/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        if (allowedOriginPatterns != null && allowedOriginPatterns.length > 0 && allowedOriginPatterns[0] != null && !allowedOriginPatterns[0].isBlank()) {
            registration.allowedOriginPatterns(allowedOriginPatterns);
        } else if (allowedOrigins != null && allowedOrigins.length > 0) {
            registration.allowedOrigins(allowedOrigins);
        }
    }
}
