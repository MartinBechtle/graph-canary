package com.martinbechtle.graphcanary.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.PreDestroy;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author martin
 */
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfig.class);

    // make this configurable in the future?
    private final ExecutorService asyncEmailExecutor = Executors.newFixedThreadPool(4);

    @Bean
    @Profile("!test")
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
        return new SpringEmailService(mailSender, asyncEmailExecutor, emailProperties, startupClock);
    }

    @Bean
    public StartupClock startupClock() {

        return new StartupClock(Clock.systemDefaultZone());
    }

    @PreDestroy
    public void destroy() {

        logger.info("Shutting down email async executor");
        asyncEmailExecutor.shutdownNow();
        logger.info("Done");
    }
}
