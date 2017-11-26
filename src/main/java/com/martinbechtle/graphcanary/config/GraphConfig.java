package com.martinbechtle.graphcanary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.graphcanary.graph.InMemoryDynamicGraphService;
import com.martinbechtle.graphcanary.graph.StaticGraphService;
import com.martinbechtle.graphcanary.monitor.CanaryMonitor;
import com.martinbechtle.graphcanary.monitor.CanaryRetriever;
import com.martinbechtle.graphcanary.monitor.RetrofitRestClient;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Martin Bechtle
 */
@Configuration
@EnableScheduling
public class GraphConfig {

    private static final Logger logger = LoggerFactory.getLogger(GraphConfig.class);

    @Bean
    public GraphService graphProvider(CanaryProperties canaryProperties) {

        if (canaryProperties.isFake()) {

            logger.info("Using static graph service with a fake graph. " +
                    "If this is unwanted please set property canary.fake to false");

            return new StaticGraphService();
        }
        return new InMemoryDynamicGraphService();
    }

    @Bean
    public CanaryMonitor canaryMonitor(CanaryProperties canaryProperties, CanaryRetriever canaryRetriever) {

        if (canaryProperties.isFake()) {
            return null;
        }
        int threadPoolSize = canaryProperties.getThreads();

        logger.info("Initializing CanaryMonitor with {} threads", threadPoolSize);

        return new CanaryMonitor(
                canaryProperties,
                newScheduledThreadPool(threadPoolSize),
                canaryRetriever);
    }

    @Bean
    public Retrofit retrofit(ObjectMapper objectMapper, HttpClientProperties httpClientProperties) {

        ConnectionPool connectionPool = new ConnectionPool(httpClientProperties.getMaxConnections(),
                httpClientProperties.getKeepAliveDurationInMillis(), MILLISECONDS);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(httpClientProperties.getConnectTimeoutInMillis(), MILLISECONDS)
                .readTimeout(httpClientProperties.getReadTimeoutInMillis(), MILLISECONDS)
                .writeTimeout(httpClientProperties.getWriteTimeoutInMillis(), MILLISECONDS)
                .connectionPool(connectionPool)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    @Bean
    public RetrofitRestClient retrofitRestClient(Retrofit retrofit) {

        return retrofit.create(RetrofitRestClient.class);
    }

}
