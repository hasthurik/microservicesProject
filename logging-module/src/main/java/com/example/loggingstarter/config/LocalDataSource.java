//package com.example.loggingstarter.config;
//
//import com.github.dockerjava.api.model.ExposedPort;
//import com.github.dockerjava.api.model.PortBinding;
//import com.github.dockerjava.api.model.Ports;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import javax.sql.DataSource;
//
////конфиг для поднятия бд в докере
//@Configuration
//@Slf4j
//public class LocalDataSource {
//
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public PostgreSQLContainer<?> pgContainer(
//            @Value("${postgres.container.image:postgres:15-alpine}") String image) {
//        var container = new PostgreSQLContainer<>(DockerImageName.parse(image))
//                .withDatabaseName("log_db")
//                .withUsername("postgres")
//                .withPassword("postgres")
//                .withReuse(true);
//        return container;
//    }
//
//    @Bean
//    public DataSource postgresDataSource(PostgreSQLContainer<?> container) {
//        log.info("JDBC URL: {}", container.getJdbcUrl());
//        log.info("USER: {}", container.getUsername());
//        log.info("PASS: {}", container.getPassword());
//
//        var hikari = new HikariConfig();
//        hikari.setJdbcUrl(container.getJdbcUrl());
//        hikari.setUsername(container.getUsername());
//        hikari.setPassword(container.getPassword());
//        return new HikariDataSource(hikari);
//    }
//}
//
