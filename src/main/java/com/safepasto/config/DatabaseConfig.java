package com.safepasto.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("prod")
    public DataSource dataSource() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new RuntimeException("DATABASE_URL no está configurada");
        }

        URI dbUri = new URI(databaseUrl);

        String userInfo = dbUri.getUserInfo();
        String username = userInfo != null && userInfo.contains(":") ? userInfo.split(":")[0] : userInfo;
        String password = userInfo != null && userInfo.contains(":") ? userInfo.split(":")[1] : null;

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

        return DataSourceBuilder.create()
                .url(jdbcUrl.toString())
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}