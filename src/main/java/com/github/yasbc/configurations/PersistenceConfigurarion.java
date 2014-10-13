package com.github.yasbc.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class PersistenceConfigurarion {

    @Value("${persistence.unit.name:yasbc-dev-pu}")
    private String unitName;

    @Bean
    public EntityManager getEntityManager() {
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory(unitName);

        return factory.createEntityManager();
    }
}
