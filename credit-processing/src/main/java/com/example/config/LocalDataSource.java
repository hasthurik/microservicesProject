package com.example.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
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
public class LocalDataSource {

    @Bean("pgContainer")
    public PostgreSQLContainer<?> postgreSQLContainer(@Value("${postgres.container.image}")
                                                      String primaryPostgreSQLContainer) {
        DockerImageName postgres =
                DockerImageName.parse(primaryPostgreSQLContainer).asCompatibleSubstituteFor("postgres");
        final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(postgres);
        postgreSQLContainer.withReuse(true); //повторное использование контейнера
        postgreSQLContainer.withCreateContainerCmdModifier(cmd ->
                cmd.getHostConfig()
                        .withPortBindings(new PortBinding(
                                Ports.Binding.bindPort(5438),
                                new ExposedPort(5432)
                        ))
        );
        postgreSQLContainer.withDatabaseName("credit_db");
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
