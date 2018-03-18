package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.canary.CanaryService;
import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.rest.PathProvider;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Allows testing the API (controllers) with actual HTTP requests,
 * but with the ability to mock the underlying service layer
 *
 * @author Martin Bechtle
 */
@ComponentScan(basePackages = {"com.martinbechtle.graphcanary"})
@EnableAutoConfiguration
@Import({GraphCanaryConfig.class})
public class ApiTestConfig {

    @Bean
    @Primary
    public CanaryService canaryService() {

        return Mockito.mock(CanaryService.class);
    }

    @Bean
    @Primary
    public EmailService emailService() {

        return Mockito.mock(EmailService.class);
    }

    @Value("${server.contextPath}")
    String contextPath;

    @Bean
    public PathProvider webPathProvider() throws UnknownHostException {

        final String ipAddress = InetAddress.getLocalHost().getHostAddress();
        final String baseUrl = "http://" + ipAddress;
        final String safeContextPath = contextPath.endsWith("/") ? contextPath : contextPath + "/";

        return (path, serverPort) -> baseUrl + ":" + serverPort + safeContextPath + path;
    }

}
