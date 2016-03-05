package com.samhalperin.phillybikesharemap.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by sqh on 9/27/15.
 */
public class BikeClient {
    private static final String TAG = "BikeClient";

    public static Endpoints getApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
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