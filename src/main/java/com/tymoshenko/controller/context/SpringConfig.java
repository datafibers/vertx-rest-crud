package com.tymoshenko.controller.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Simple Java Spring configuration to be used for the Spring example application. This configuration is mainly
 * composed of a database configuration and initial population via the script "products.sql" of the database for
 * querying by our Spring service bean.
 * <p>
 * The Spring service bean and repository are scanned for via @EnableJpaRepositories and @ComponentScan annotations
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
@Configuration
@ComponentScan("com.tymoshenko.controller.service")
@EnableJpaRepositories(basePackages = {"com.tymoshenko.controller.repository"})
@PropertySource(value = {"classpath:jdbc.properties"})
public class SpringConfig {

    @Autowired
    private Environment jdbcProperties;

    @Bean
    @Autowired
    public DataSource dataSource(DatabasePopulator populator) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcProperties.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(jdbcProperties.getProperty("jdbc.url"));
        dataSource.setUsername(jdbcProperties.getProperty("jdbc.username"));
        dataSource.setPassword(jdbcProperties.getProperty("jdbc.password"));
        DatabasePopulatorUtils.execute(populator, dataSource);
        return dataSource;
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.TRUE);
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.tymoshenko.model");
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", jdbcProperties.getProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.show_sql", jdbcProperties.getProperty("hibernate.show_sql"));
        factory.setJpaProperties(jpaProperties);
        return factory;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    @Bean
    @Autowired
    public DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(false);
        populator.addScript(new ClassPathResource("init-db.sql"));
        return populator;
    }
}
