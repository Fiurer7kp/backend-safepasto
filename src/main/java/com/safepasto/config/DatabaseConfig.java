package com.safepasto.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("prod")
    public DataSource dataSource() throws URISyntaxException {
        // Support both Render's DATABASE_URL and possible JDBC_DATABASE_URL
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            databaseUrl = System.getenv("JDBC_DATABASE_URL");
        }

        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new RuntimeException("DATABASE_URL no está configurada");
        }

        URI dbUri = new URI(databaseUrl);

        String userInfo = dbUri.getUserInfo();
        String username = null;
        String password = null;
        if (userInfo != null) {
            String[] parts = userInfo.split(":", 2); // split only on the first ':'
            username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            if (parts.length > 1) {
                password = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            }
        }

        int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
        String path = dbUri.getPath(); // includes leading '/' + db name
        String query = dbUri.getQuery(); // may contain e.g., sslmode=require

        StringBuilder jdbcUrl = new StringBuilder();
        jdbcUrl.append("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(":")
                .append(port)
                .append(path != null ? path : "");

        if (query != null && !query.isBlank()) {
            jdbcUrl.append("?").append(query);
        }

        // Basic startup diagnostics (will appear in logs)
        System.out.println("[DatabaseConfig] Host: " + dbUri.getHost() + 
                ", Port: " + port + 
                ", DB: " + (path != null ? path.replaceFirst("/", "") : ""));
        if (query != null && !query.isBlank()) {
            System.out.println("[DatabaseConfig] Query params: " + query);
        }
        System.out.println("[DatabaseConfig] JDBC URL: " + jdbcUrl);

        return DataSourceBuilder.create()
                .url(jdbcUrl.toString())
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}