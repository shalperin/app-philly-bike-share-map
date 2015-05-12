package com.samhalperin.phillybikesharemap;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * Created by sqh on 5/8/15.
 */
public class StationDataTask extends AsyncTask<String, Void, Station[]> {

    MapsActivity mHandler;

    public StationDataTask(MapsActivity context) {
        mHandler = context;
    }

    private static boolean DEBUG = false;

    @Override
    protected Station[] doInBackground(String... params) {

        if (DEBUG) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return debuggingStations();
        } else {
            AndroidHttpClient client = AndroidHttpClient.newInstance("");
            HttpGet request = new HttpGet(params[0]);
            StationJsonResponseHandler responseHandler = new StationJsonResponseHandler();
            try {
                return client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
            Log.e("pbsm", "Uh oh, fell through to nodata.");
            return new Station[0];
        }
    }

    private Station[] debuggingStations() {
        return new Station[]  {
                new Station(MapsActivity.PHILLY,
                        "1234 fake street",
                        20,
                        30,
                        "Active"
                ),
                new Station(
                        new LatLng(39.95378, -75.16374),
                        "1401 JFK debug",
                        30,
                        40,
                        "Unknown"
                )
        };
    }

    @Override
    protected void onPostExecute(Station[] stations) {
        mHandler.loadStationData(stations);
    }

    public interface StationDataLoader{
        void loadStationData(Station[] stations);
    }

}
