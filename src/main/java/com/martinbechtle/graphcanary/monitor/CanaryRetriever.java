package com.martinbechtle.graphcanary.monitor;

import com.martinbechtle.graphcanary.config.CanaryEndpoint;
import com.martinbechtle.graphcanary.graph.GraphService;
import com.martinbechtle.jcanary.api.Canary;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * Retrieves a {@link Canary} from a {@link CanaryEndpoint} and updates the {@link GraphService} with the new data.
 * Thread safe.
 *
 * @author Martin Bechtle
 */
@Component
public class CanaryRetriever {

    private static final Logger logger = LoggerFactory.getLogger(CanaryRetriever.class);

    private final GraphService graphService;

    private final Retrofit retrofit;

    private final RetrofitRestClient retrofitRestClient;

    public CanaryRetriever(GraphService graphService, Retrofit retrofit, RetrofitRestClient retrofitRestClient) {

        this.graphService = graphService;
        this.retrofit = retrofit;
        this.retrofitRestClient = retrofitRestClient;
    }

    public void retrieveAndUpdate(CanaryEndpoint canaryEndpoint) {

        try {
            Response<ResponseBody> response = retrofitRestClient
                    .getCanary(canaryEndpoint.getUrl(), canaryEndpoint.getSecret())
                    .execute();

            return;
        }
        catch (IOException e) {
            logger.warn("Network error while retrieving canary for " + canaryEndpoint.getName(), e);
        }
        catch (RuntimeException e) {
            logger.warn("Http error while retrieving canary for " + canaryEndpoint.getName(), e);
        }
        // TODO insert some error in the graph
    }
}
