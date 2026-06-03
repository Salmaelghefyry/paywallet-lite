package com.paylogic.paywalletlite.config.root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

<<<<<<< Updated upstream
/**
 * Configuration applicative générale.
 * Définit les beans fondamentaux de l'application.
 */
=======
import javax.annotation.PostConstruct;

>>>>>>> Stashed changes
@Configuration
@PropertySource({
        "classpath:properties/application.properties",
        "classpath:properties/application-${spring.profiles.active:dev}.properties"
})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}