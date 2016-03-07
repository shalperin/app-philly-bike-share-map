package com.samhalperin.phillybikesharemap.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.rest.pojo.BikeData;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by sqh on 9/27/15.
 */
public class BikeClient {
    private static final String TAG = "BikeClient";
    private static final int CACHE_SIZE =10 * 1024 * 1024; // 10 MiB
    private BikeClientResponseHandler handler;
    private Context context;

    public BikeClient(Context context) {
        this.context = context;
    }

    public void setResponseHandler(BikeClientResponseHandler handler) {
        this.handler = handler;
    }

    public void fetch() {
        Endpoints api = getApi(context);
        Call<BikeData> call = api.getBikeData();
        call.enqueue(new Callback<BikeData>() {
            @Override
            public void onResponse(Call<BikeData> call, retrofit2.Response<BikeData> response) {
                try {
                    BikeData data = response.body();
                    handler.onBikeApiFetchSuccess(data);
                } catch (Exception e) {
                    handler.onBikeApiFetchFailure("Ooops! Sorry, parse error.");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BikeData> call, Throwable t) {
                handler.onBikeApiFetchFailure("Ooops! Sorry, network error.");
                t.printStackTrace();
            }
        });
    }


    private static Endpoints getApi(Context context) {
        Cache cache = new Cache(new File(context.getCacheDir(), "http"), CACHE_SIZE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                //[DEBUGGING].addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BikeShareApplication.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(Endpoints.class);
    }

    private interface Endpoints {
        @GET("/bike-share-stations/v1")
        Call<BikeData> getBikeData();
    }

    public interface BikeClientResponseHandler {
        void onBikeApiFetchSuccess(BikeData bikedata);

        void onBikeApiFetchFailure(String msg);
    }
}