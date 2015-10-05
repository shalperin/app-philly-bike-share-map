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

/**
 * Created by sqh on 9/27/15.
 */
public class BikeShareApplication extends Application {
    public static final LatLng PHILLY = new LatLng(39.9500, -75.1667);
    public static final int DEFAULT_ZOOM_LEVEL = 12;
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;
    private Tracker mTracker;

    private Observable<Station[]> mStationObservable = Observable.create(new Observable.OnSubscribe<Station[]>() {
        @Override
        public void call(Subscriber<? super Station[]> subscriber) {
            if (!subscriber.isUnsubscribed()) {
                try {
                    BikeData b = BikeClient.getBikeApiClient().bikeData();
                    List<Station> S = new ArrayList<>();
                    for (Feature f : b.getFeatures()) {
                        Properties p = f.getProperties();
                        Geometry g = f.getGeometry();
                        Station s = new Station(  //refactor - build this into the POJO.
                                new LatLng(
                                        g.getCoordinates().get(LAT_INDEX),
                                        g.getCoordinates().get(LNG_INDEX)),
                                p.getAddressStreet(),
                                p.getBikesAvailable(),
                                p.getDocksAvailable(),
                                p.getKioskPublicStatus()
                        );
                        S.add(s);
                    }
                    subscriber.onNext(S.toArray(new Station[S.size()]));
                    subscriber.onCompleted();
                } catch (Exception e) {

                    subscriber.onError(e);
                }
            }
        }
    });


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public Observable<Station[]> getStationObservable() {
        return mStationObservable;
    }
}
