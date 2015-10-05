package com.samhalperin.phillybikesharemap.data;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.retrofit.BikeClient;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Feature;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Geometry;
import com.samhalperin.phillybikesharemap.retrofit.pojo.Properties;
import com.samhalperin.phillybikesharemap.ui.MapsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqh on 5/8/15.
 */
public class StationTask extends AsyncTask<String, Void, Station[]> {

    MapsActivity mHandler;
    public StationTask(MapsActivity context) {
        mHandler = context;
    }
    private static boolean DEBUG = false;
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;
    private static final String BIKES_AVAIALBLE_TAG = "bikesAvailable";
    private static final String DOCKS_AVAIALABLE_TAG = "docksAvailable";
    private static final String KIOSK_PUBLIC_STATUS_TAG ="kioskPublicStatus";

    @Override
    protected Station[] doInBackground(String... params) {
        BikeData b = BikeClient.getBikeApiClient().bikeData();
        List<Station> S= new ArrayList<Station>();
        for (Feature f: b.getFeatures())  {
            Properties p = f.getProperties();
            Geometry g = f.getGeometry();
            Station s = new Station(
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
        return S.toArray(new Station[S.size()]);
    }


    private Station[] debuggingStations() {
        return new Station[]  {

                new Station(BikeShareApplication.PHILLY,
                        "1234 fake street",
                        20,
                        30,
                        "Available"
                ),
                new Station(BikeShareApplication.PHILLY,
                        "1234 fake street",
                        20,
                        30,
                        "Debug"
                ),
                new Station(
                        new LatLng(39.95378, -75.16374),
                        "1401 JFK debug",
                        30,
                        40,
                        "ComingSoon"
                ),
                new Station(
                        new LatLng(39.93378, -75.16374),
                        "5586 unreal street",
                        30,
                        40,
                        "PartialService"
                ),
                new Station(
                        new LatLng(39.91378, -75.16374),
                        "987 philly street",
                        30,
                        40,
                        "Unavailable"
                ),
                new Station(
                        new LatLng(39.88378, -75.16374),
                        "987 philly street",
                        30,
                        40,
                        "SpecialEvent"
                )


        };
    }

    @Override
    protected void onPostExecute(Station[] stations) {
       // mHandler.loadStationData(stations);
    }

    public interface StationDataLoader{
        void loadStationData(Station[] stations);
    }

}
