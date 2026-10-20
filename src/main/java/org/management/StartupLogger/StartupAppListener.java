package org.management.startuplogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.time.Instant;


public class StartupAppListener implements ApplicationListener<ApplicationReadyEvent>{
    private static final Logger log = LoggerFactory.getLogger(StartupAppListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        log.info("==========================================================================");
        log.info("Сервис успешно запущен!");
        log.info("Активные профили: {}", String.join(", ", environment.getActiveProfiles()));
        log.info("Имя приложения: {}", environment.getProperty("spring.application.name", "unknown"));
        log.info("Параметры БД: JDBC URL = {}", environment.getProperty("spring.datasource.url",
                "Not Configured"));
        log.info("Время запуска контекста: {} мс", uptime);
        log.info("Текущее время сервера: {}", Instant.now());
        log.info("==========================================================================");

    }
}
