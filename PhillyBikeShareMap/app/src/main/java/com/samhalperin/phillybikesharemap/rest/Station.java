package com.samhalperin.phillybikesharemap.rest;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by sqh on 5/8/15.
 *
 * REFACTOR? This could be rolled into the retrofit POJO at some point.
 */
public class Station implements ClusterItem {
    private LatLng mLatLng;
    private String mAddressStreet;
    private int mBikesAvailable;
    private int mDocksAvailable;
    private statuses mStatus;
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;
    private String mId;

    public enum statuses {
        ACTIVE,
        SPECIAL_EVENT,
        PARTIAL_SERVICE,
        UNAVAILABLE,
        COMING_SOON,
        UNKNOWN,
        DEBUG
    }

    private static String ACTIVE_TAG = "Active";
    private static String SPECIAL_EVENT_TAG = "SpecialEvent"; // guess
    private static String PARTIAL_SERVICE_TAG = "PartialService"; //guess
    private static String UNAVAILABLE_TAG = "Unavailable";
    private static String COMING_SOON_TAG = "ComingSoon";
    private static String DEBUG_TAG = "Debug";


    public Station(LatLng latLng, String addressStreet, int bikesAvailable, int docksAvailable,
                   String status, String id) {
        mLatLng = latLng;
        mAddressStreet = addressStreet;
        mBikesAvailable = bikesAvailable;
        mDocksAvailable = docksAvailable;

        if (status.equals(ACTIVE_TAG)) {
            mStatus = statuses.ACTIVE;
        } else if (status.equals(SPECIAL_EVENT_TAG)) {
            mStatus = statuses.SPECIAL_EVENT;
        } else if (status.equals(PARTIAL_SERVICE_TAG)) {
            mStatus = statuses.PARTIAL_SERVICE;
        } else if (status.equals(UNAVAILABLE_TAG)) {
            mStatus = statuses.UNAVAILABLE;
        } else if (status.equals(COMING_SOON_TAG)) {
            mStatus = statuses.COMING_SOON;
        }else if (status.equals(DEBUG_TAG)) {
            mStatus = statuses.DEBUG;
        }
        else{
            mStatus = statuses.UNKNOWN;
        }

        mId = id;
    }

    public LatLng getPosition() {
        return mLatLng;
    }

    public String getStreet() {
        return mAddressStreet;
    }

    public String getInfo() {
        if (mStatus == statuses.ACTIVE) {
            return String.format("%d bikes available / %d open docks", mBikesAvailable, mDocksAvailable);
        } else {
            return String.format("%d bikes available / %d open docks (%s)", mBikesAvailable, mDocksAvailable, getHumanizedStatus());
        }
    }

    public statuses getStatus() {
        return mStatus;
    }

    public String getHumanizedStatus() {
        if (mStatus == Station.statuses.ACTIVE) {
            return "Active";
        } else if (mStatus == Station.statuses.SPECIAL_EVENT){
            return "Special Event";
        } else if (mStatus == Station.statuses.PARTIAL_SERVICE){
            return "Partial Service";
        }  else if (mStatus == Station.statuses.COMING_SOON){
            return "Coming Soon";
        }  else if (mStatus == Station.statuses.UNAVAILABLE){
            return "Kiosk Unavailable";
        }  else {
            return "Status Unknown";
        }
    }

    public String getId() {
        return mId;
    }

}
