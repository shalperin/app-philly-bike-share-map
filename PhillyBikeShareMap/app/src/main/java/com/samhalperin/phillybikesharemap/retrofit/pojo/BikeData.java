

package com.samhalperin.phillybikesharemap.retrofit.pojo;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.samhalperin.phillybikesharemap.retrofit.Station;


@Generated("org.jsonschema2pojo")
public class BikeData {
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;
    private static final String TAG = "BikeData";

    @SerializedName("features")
    @Expose
    private List<Feature> features = new ArrayList<Feature>();

    /**
     * @return The features
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * @param features The features
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Station[] asArray() {
        List<Station> S = asList();
        return S.toArray(new Station[S.size()]);
    }

    public List<Station> asList() {

        List<Station> S = new ArrayList<Station>();
        StringBuilder log = new StringBuilder().append("empty log");
        try {  // be a little careful about bad data here.
            log = new StringBuilder();
            for (Feature f : getFeatures()) {
                log.append("getProperties");
                Properties p = f.getProperties();
                log.append("getGeometry");
                Geometry g = f.getGeometry();
                log.append("getLatLng");
                LatLng coords =new LatLng(
                        g.getCoordinates().get(LAT_INDEX),
                        g.getCoordinates().get(LNG_INDEX));
                log.append("getAddressStreet");
                String addressStreet =p.getAddressStreet();
                log.append("getBikesAvailable");
                Integer bikesAvaialable = p.getBikesAvailable();
                log.append("getDocksAvailable");
                Integer docksAvailable = p.getDocksAvailable();
                log.append("getKioskPublicStatus");
                String status = p.getKioskPublicStatus();
                Integer id = p.getKioskId();

                Station s = new Station(
                        coords,
                        addressStreet,
                        bikesAvaialable,
                        docksAvailable,
                        status,
                        // using shared prefs for model, so string is more convenient.
                        Integer.toString(id));
                S.add(s);
            }
        } catch (Exception e) {
            Log.e(TAG, log.toString());
            e.printStackTrace();
        }
        return S;
    }

    public Map<String, Station> asMap() {
        List<Station> S = asList();

        HashMap<String, Station> map = new HashMap<String, Station>();
        for (Station station : S) {
            map.put(station.getId(), station);
        }
        return map;
    }

}