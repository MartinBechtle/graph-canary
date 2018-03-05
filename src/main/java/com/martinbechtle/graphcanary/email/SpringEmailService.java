package com.martinbechtle.graphcanary.email;

import com.martinbechtle.graphcanary.graph.GraphEdge;
import com.martinbechtle.jcanary.api.CanaryResult;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author martin
 */
public class SpringEmailService implements EmailService {

    // TODO ignore any email if startup is recent

    private static final Logger logger = LoggerFactory.getLogger(SpringEmailService.class);

    private final JavaMailSender mailSender;

    private final String recipientAddress;

    private static final String SUBJECT = "Graph-canary notification";

    public SpringEmailService(JavaMailSender mailSender, String recipientAddress) {

        this.mailSender = mailSender;
        this.recipientAddress = recipientAddress;
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

        logger.debug("Sending email: {}", text);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientAddress);
        message.setSubject(SUBJECT);
        message.setText(text);
//        mailSender.send(message);
    }
}
