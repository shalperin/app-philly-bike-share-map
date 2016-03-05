package com.samhalperin.phillybikesharemap.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by sqh on 9/27/15.
 */
public class BikeClient {
    private static final String TAG = "BikeClient";
    private static final String CACHE_AGE= "300";
    private static final int CACHE_SIZE =10 * 1024 * 1024; // 10 MiB
    private static final int READ_TIMEOUT = 120;
    private static final int CONNECT_TIMEOUT = 120;

    }
    */
    public static Endpoints getApi(Context context) {
        Cache cache = new Cache(new File(context.getCacheDir(), "http"), CACHE_SIZE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                //.addNetworkInterceptor(new CachingControlInterceptor())
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

    public interface Endpoints {
        @GET("/bike-share-stations/v1")
        Call<BikeData> getBikeData();
    }
}