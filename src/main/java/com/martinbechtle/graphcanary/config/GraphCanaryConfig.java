package com.martinbechtle.graphcanary.config;

import com.martinbechtle.graphcanary.email.EmailConfig;
import com.martinbechtle.graphcanary.email.EmailService;
import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.graph.InMemoryDynamicGraphService;
import com.martinbechtle.graphcanary.graph.StaticGraphService;
import com.martinbechtle.graphcanary.monitor.CanaryMonitor;
import com.martinbechtle.graphcanary.monitor.CanaryRetriever;
import com.martinbechtle.graphcanary.monitor.HttpClientProperties;
import com.martinbechtle.graphcanary.warning.InMemoryDynamicWarningService;
import com.martinbechtle.graphcanary.warning.StaticWarningService;
import com.martinbechtle.graphcanary.warning.WarningService;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Martin Bechtle
 */
@Configuration
@Import({
        EmailConfig.class,
        JacksonConfig.class,
        WebConfig.class
})
public class GraphCanaryConfig {

    private static final Logger logger = LoggerFactory.getLogger(GraphCanaryConfig.class);

    private ScheduledExecutorService canaryMonitorThreadPool;

    @Bean
    public GraphService graphProvider(CanaryProperties canaryProperties) {

        if (canaryProperties.isFake()) {

            logger.info("Using static graph service with a fake graph. " +
                    "If this is unwanted please set property canary.fake to false");

            return new StaticGraphService();
        }
        return new InMemoryDynamicGraphService();
    }

    @Bean("canaryMonitorThreadPool")
    @Profile("!test")
    public ScheduledExecutorService canaryMonitorThreadPool(CanaryProperties canaryProperties) {

        int threadPoolSize = canaryProperties.getThreads();
        logger.info("Initializing CanaryMonitor with {} threads", threadPoolSize);
        canaryMonitorThreadPool = newScheduledThreadPool(threadPoolSize);
        return canaryMonitorThreadPool;
    }

    @Bean
    @Profile("!test")
    public CanaryMonitor canaryMonitor(CanaryProperties canaryProperties,
                                       CanaryRetriever canaryRetriever,
                                       ScheduledExecutorService canaryMonitorThreadPool) {

        if (canaryProperties.isFake()) {
            return null;
        }
        return new CanaryMonitor(canaryProperties, canaryMonitorThreadPool, canaryRetriever);
    }

    @Bean
    public WarningService warningService(CanaryProperties canaryProperties, EmailService emailService) {

        if (canaryProperties.isFake()) {

            logger.info("Using static warning service with fake warnings. " +
                    "If this is unwanted please set property canary.fake to false");

            return new StaticWarningService();
        }
        return new InMemoryDynamicWarningService(emailService);
    }

    @Bean
    public OkHttpClient okHttpClient(HttpClientProperties httpClientProperties) {

        ConnectionPool connectionPool = new ConnectionPool(httpClientProperties.getMaxConnections(),
                httpClientProperties.getKeepAliveDurationInMillis(), MILLISECONDS);

        return new OkHttpClient()
                .newBuilder()
                .connectTimeout(httpClientProperties.getConnectTimeoutInMillis(), MILLISECONDS)
                .readTimeout(httpClientProperties.getReadTimeoutInMillis(), MILLISECONDS)
                .writeTimeout(httpClientProperties.getWriteTimeoutInMillis(), MILLISECONDS)
                .connectionPool(connectionPool)
                .build();
    }

    @PreDestroy
    public void destroy() {

        logger.info("Shutting down email async executor");
        canaryMonitorThreadPool.shutdown();
        logger.info("Done");
    }

}
