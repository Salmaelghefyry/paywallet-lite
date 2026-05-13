package com.paylogic.paywalletlite.config.root;

import com.paylogic.paywalletlite.config.database.DataSourceConfig;
import com.paylogic.paywalletlite.config.database.JpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PropertyConfig.class,
        DataSourceConfig.class,
        JpaConfig.class
})
@ComponentScan("com.paylogic.paywalletlite.service") // services et repositories
public class RootConfig {
}