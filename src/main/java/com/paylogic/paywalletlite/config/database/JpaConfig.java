package com.paylogic.paywalletlite.config.database;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.paylogic.paywalletlite.domain")   // scan des entités
public class JpaConfig {

    @Value("${jpa.database-platform}")
    private String dialect;

    @Value("${jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${jpa.show-sql}")
    private boolean showSql;

    @Value("${jpa.format-sql}")
    private boolean formatSql;

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.paylogic.paywalletlite.domain");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", dialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        jpaProperties.setProperty("hibernate.show_sql", String.valueOf(showSql));
        jpaProperties.setProperty("hibernate.format_sql", String.valueOf(formatSql));
        jpaProperties.setProperty("hibernate.default_schema", "PWL_APP");   // adapter si besoin
        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}