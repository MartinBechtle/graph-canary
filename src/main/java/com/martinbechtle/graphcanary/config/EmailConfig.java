package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.email.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author martin
 */
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfig.class);

    @Bean
    public EmailService emailService(EmailProperties emailProperties,
                                     JavaMailSender mailSender,
                                     StartupClock startupClock) {

        boolean emailFromMissing = isEmpty(emailProperties.getFrom());
        boolean emailToMissing = isEmpty(emailProperties.getTo());
        if (emailFromMissing) {
            logger.warn("Email from is not set. No emails will be sent");
        }
        else if (emailToMissing) {
            logger.warn("Email to is not set. No emails will be sent");
        }
        if (emailFromMissing || emailToMissing) {
            return new NoOpEmailService();
        }
        Clock.systemDefaultZone();
        ExecutorService asyncEmailExecutor = Executors.newFixedThreadPool(4); // make this configurable in the future?
        return new SpringEmailService(mailSender, asyncEmailExecutor, emailProperties, startupClock);
    }

    @Bean
    public StartupClock startupClock() {

        return new StartupClock(Clock.systemDefaultZone());
    }
}
