package org.management.startuplogger;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(StartupLoggerAutoConfiguration.class)
@ConditionalOnProperty(
        prefix = "custom.startup-logger",
        havingValue = "true",
        name = "enabled",
        matchIfMissing = true)
public class StartupLoggerAutoConfiguration {
    @Bean
    StartupAppListener startupLoggingListener() {
        return new StartupAppListener();
    }
}
