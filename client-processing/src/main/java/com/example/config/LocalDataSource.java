package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@Slf4j
@Configuration
//@Profile("!compose")
public class LocalDataSource {

    @Bean("pgContainer")
    public PostgreSQLContainer<?> postgreSQLContainer(@Value("${postgres.container.image}")
                                                      String primaryPostgreSQLContainer) {
        DockerImageName postgres =
                DockerImageName.parse(primaryPostgreSQLContainer).asCompatibleSubstituteFor("postgres");
        final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(postgres);
        postgreSQLContainer.withReuse(true); //повторное использование контейнера
        postgreSQLContainer.withExposedPorts(5432, 5433);
        postgreSQLContainer.withDatabaseName("client_db");
        postgreSQLContainer.start();
        log.info("Jdbc url: {}", postgreSQLContainer.getJdbcUrl());
        log.info("Jdbc user: {}", postgreSQLContainer.getUsername());
        log.info("Jdbc password: {}", postgreSQLContainer.getPassword());
        return postgreSQLContainer;
    }

    @Bean()
    public DataSource postgresDataSource(@Qualifier("pgContainer") PostgreSQLContainer<?> postgreSQLContainer) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setUsername(postgreSQLContainer.getUsername());
        hikariConfig.setPassword(postgreSQLContainer.getPassword());
        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());

        return new HikariDataSource(hikariConfig);
    }
}
