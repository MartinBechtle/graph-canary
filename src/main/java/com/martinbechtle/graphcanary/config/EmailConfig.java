package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.email.NoOpEmailService;
import com.martinbechtle.graphcanary.email.SpringEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author martin
 */
@Configuration
public class EmailConfig {

    @Bean
    public EmailService emailService(
            @Value("${canary.mailrecipient:''}") String mailRecipient,
            JavaMailSender mailSender) {

        if (mailRecipient.isEmpty()) {
            return new NoOpEmailService();
        }
        return new SpringEmailService(mailSender, mailRecipient);
    }
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("my.gmail@gmail.com");
//        mailSender.setPassword("password");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
}
