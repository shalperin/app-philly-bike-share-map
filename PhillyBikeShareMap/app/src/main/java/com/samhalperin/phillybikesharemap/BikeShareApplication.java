package com.samhalperin.phillybikesharemap;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;
import com.samhalperin.phillybikesharemap.data.Station;
import com.samhalperin.phillybikesharemap.retrofit.BikeClient;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Feature;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Geometry;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Properties;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by sqh on 9/27/15.
 */
public class BikeShareApplication extends Application {
    public static final LatLng PHILLY = new LatLng(39.9500, -75.1667);
    public static final int DEFAULT_ZOOM_LEVEL = 12;
    private Tracker mTracker;

    private Observable<BikeData> bikeDataObservable = Observable.create(new Observable.OnSubscribe<BikeData>() {
       public void call(Subscriber<? super BikeData> subscriber) {
           if (!subscriber.isUnsubscribed()) {
               try {
                   BikeData b = BikeClient.getBikeApiClient().bikeData();
                   subscriber.onNext(b);
                   subscriber.onCompleted();
               } catch (Exception e) {
                   e.printStackTrace();
                   subscriber.onError(e);
               }
           }
       }
    });

    private Observable<Station> allStations =
            bikeDataObservable.flatMap(BikeData.GET_FEATURES).map(Station.CREATE);


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public Observable<Station> getStationObservable() {
        return allStations;
    }
}
