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


/**
 * Created by sqh on 9/27/15.
 */
public class BikeShareApplication extends Application {
    public static final LatLng PHILLY = new LatLng(39.9500, -75.1667);
    public static final int DEFAULT_ZOOM_LEVEL = 12;
    private Tracker mTracker;
    public static final String BASE_URL = "http://api.phila.gov";


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
