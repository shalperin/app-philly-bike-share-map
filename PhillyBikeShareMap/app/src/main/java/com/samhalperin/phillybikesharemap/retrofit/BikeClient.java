package com.samhalperin.phillybikesharemap.retrofit;

import retrofit.RestAdapter;

/**
 * Created by sqh on 9/27/15.
 */
public class BikeClient {
    private static BikeApiInterface sBikeService;

    public static BikeApiInterface getBikeApiClient() {
        if (sBikeService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.phila.gov")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            sBikeService = restAdapter.create(BikeApiInterface.class);
        }
        return sBikeService;
    }
}