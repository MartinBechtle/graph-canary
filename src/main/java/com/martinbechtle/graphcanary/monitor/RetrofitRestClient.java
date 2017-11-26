package com.martinbechtle.graphcanary.monitor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author Martin Bechtle
 */
public interface RetrofitRestClient {

    @GET
    Call<ResponseBody> getCanary(@Url String url, @Query("secret") String secret);
}
