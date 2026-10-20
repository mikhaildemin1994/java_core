package org.management.startuplogger;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.startup-logger")
public record StartupLoggerProperties(
        boolean enabled
) {}
