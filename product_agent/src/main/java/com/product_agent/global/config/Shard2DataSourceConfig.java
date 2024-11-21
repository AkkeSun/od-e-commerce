package com.product_agent.global.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
    basePackages = {"com.product_agent.product.adapter.out.persistence.jpa.shard2"},
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager"
)
public class Shard2DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.shard2")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondaryDataSource(
        @Qualifier("secondaryDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        return dataSourceProperties
            .initializeDataSourceBuilder()
            .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("secondaryDataSource") DataSource dataSource
    ) {
        return builder
            .dataSource(dataSource)
            .packages("com.product_agent.product.adapter.out.persistence.jpa.shard2")
            .persistenceUnit("secondaryEntityManager")
            .build();
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager(
        @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}