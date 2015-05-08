package com.samhalperin.phillybikesharemap;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqh on 5/8/15.
 */
public class StationJsonResponseHandler implements ResponseHandler<Station[]> {
    private static final String FEATURES_TAG = "features";
    private static final String GEOMETRY_TAG = "geometry";
    private static final String COORDINATES_TAG = "coordinates";
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;
    private static final String PROPERTIES_TAG = "properties";
    private static final String ADDRESS_STREET_TAG = "addressStreet";
    private static final String BIKES_AVAIALBLE_TAG = "bikesAvailable";
    private static final String DOCKS_AVAIALABLE_TAG = "docksAvailable";


    @Override
    public Station[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        List<Station> result = new ArrayList<Station>();
        String JSONResponse = new BasicResponseHandler()
                .handleResponse(response);

        try {
            JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();

            JSONArray stations = responseObject.getJSONArray(FEATURES_TAG);
            for(int i = 0; i<stations.length(); i++) {
                JSONObject station = (JSONObject) stations.get(i);
                JSONObject properties = station.getJSONObject(PROPERTIES_TAG);
                int bikes = properties.getInt(BIKES_AVAIALBLE_TAG);
                int docks = properties.getInt(DOCKS_AVAIALABLE_TAG);
                String addressStreet = properties.getString(ADDRESS_STREET_TAG);
                JSONObject geometry = station.getJSONObject(GEOMETRY_TAG);
                JSONArray coordinates = geometry.getJSONArray(COORDINATES_TAG);
                double lat = coordinates.getDouble(LAT_INDEX);
                double lng = coordinates.getDouble(LNG_INDEX);
                result.add(new Station(
                        new LatLng(lat, lng),
                        addressStreet,
                        bikes,
                        docks
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toArray(new Station[result.size()]);
    }
}
