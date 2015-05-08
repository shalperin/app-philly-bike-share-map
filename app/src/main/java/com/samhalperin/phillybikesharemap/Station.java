package com.samhalperin.phillybikesharemap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by sqh on 5/8/15.
 */
public class Station implements ClusterItem {
    private LatLng mLatLng;
    private String mAddressStreet;
    private int mBikesAvailable;
    private int mDocksAvailable;

    public Station(LatLng mLatLng, String mAddressStreet, int mBikesAvailable, int mDocksAvailable) {
        this.mLatLng = mLatLng;
        this.mAddressStreet = mAddressStreet;
        this.mBikesAvailable = mBikesAvailable;
        this.mDocksAvailable = mDocksAvailable;
    }

    public LatLng getPosition() {
        return mLatLng;
    }

    public String getStreet() {
        return mAddressStreet;
    }

    public String getStatus() {
        return String.format("%d bikes available / %d open docks", mBikesAvailable, mDocksAvailable);
    }
}
