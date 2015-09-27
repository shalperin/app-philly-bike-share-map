package com.samhalperin.phillybikesharemap.retrofit;

import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;

import retrofit.http.GET;

/**
 * Created by sqh on 9/27/15.
 */
public interface BikeApiInterface {
    @GET("/bike-share-stations/v1")
    BikeData bikeData();
}
