package com.martinbechtle.graphcanary;

import com.martinbechtle.graphcanary.config.CanaryProperties;
import com.martinbechtle.graphcanary.monitor.HttpClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CanaryProperties.class,
        HttpClientProperties.class
})
public class GraphCanaryApplication {

    public static void main(String[] args) {

        SpringApplication.run(GraphCanaryApplication.class, args);
    }
}
