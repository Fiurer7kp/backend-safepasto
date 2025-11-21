package com.safepasto.config;

import com.safepasto.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

  private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    logger.info("SecurityConfig - Configuring SecurityFilterChain");
    http
      .cors(c -> {
        logger.info("SecurityConfig - CORS enabled");
      })
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> {
        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        auth.requestMatchers("/auth/**", "/health", "/h2-console/**", "/ws-alertas/**").permitAll();
        auth.anyRequest().authenticated();
        logger.info("SecurityConfig - Public endpoints configured: /auth/**, /health, /h2-console/**, /ws-alertas/**");
      })
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .exceptionHandling(ex -> {
        ex.authenticationEntryPoint((request, response, authException) -> {
          logger.error("SecurityConfig - Authentication failed for path: {}, method: {}, error: {}", 
              request.getRequestURI(), request.getMethod(), authException.getMessage());
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\":\"Access denied\"}");
        });
      });

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    logger.info("SecurityConfig - SecurityFilterChain configured");
    return http.build();
  }
}