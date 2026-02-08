package com.brasil.transparente.processor.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws SQLException {
        String baseUrl = extractBaseUrl(datasourceUrl);

        createDatabaseIfNotExists(baseUrl);

        return createHikariDataSource();
    }

    private String extractBaseUrl(String url) {
        return url.substring(0, url.lastIndexOf('/')) + "/";
    }

    private void createDatabaseIfNotExists(String baseUrl) throws SQLException {
        try (Connection conn = DriverManager.getConnection(baseUrl, username, password);
             Statement stmt = conn.createStatement()) {

            String dbName = extractDatabaseName(datasourceUrl);
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        }
    }

    private String extractDatabaseName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private DataSource createHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
