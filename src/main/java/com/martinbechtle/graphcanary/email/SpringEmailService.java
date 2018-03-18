package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.CanaryResult;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * @author martin
 */
public class SpringEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SpringEmailService.class);

    // do not send emails within the 30 seconds of startup, to avoid getting flooded if the service is restarted
    // and there are existing non healthy canaries/dependencies
    private static final Duration STARTUP_EMAIL_DELAY = Duration.ofSeconds(30);

    private final ExecutorService asyncEmailExecutor;

    private final JavaMailSender mailSender;

    private final EmailProperties emailProperties;

    private final StartupClock startupClock;

    private static final String SUBJECT = "Graph-canary notification";

    public SpringEmailService(JavaMailSender mailSender,
                              ExecutorService asyncEmailExecutor,
                              EmailProperties emailProperties,
                              StartupClock startupClock) {

        this.mailSender = mailSender;
        this.asyncEmailExecutor = asyncEmailExecutor;
        this.emailProperties = emailProperties;
        this.startupClock = startupClock;
    }

    @Override
    public void notifyDependencyHealthChange(GraphEdge edge) {

        String text = String.format("Link from [%s] to [%s] is now [%s]",
                edge.getFrom(), edge.getTo(), edge.getDependencyStatus());

        send(text);
    }

    @Override
    public void notifyServiceStatusChange(String serviceName, CanaryResult result) {

        String text = String.format("Service [%s] canary endpoint retrieval is now [%s]",
                serviceName, result);

        send(text);
    }

    private void send(String text) {

        if (!startupClock.timeHasPassedSinceStartup(STARTUP_EMAIL_DELAY)) {
            logger.trace("Ignoring email because of recent startup");
            return;
        }

        logger.debug("Sending email: {}", text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailProperties.getTo());
        message.setFrom(emailProperties.getFrom());
        message.setSubject(SUBJECT);
        message.setText(text);
        asyncEmailExecutor.submit(() ->  Try.run(() -> mailSender
                .send(message))
                .onFailure(Throwable::printStackTrace));
    }

}
