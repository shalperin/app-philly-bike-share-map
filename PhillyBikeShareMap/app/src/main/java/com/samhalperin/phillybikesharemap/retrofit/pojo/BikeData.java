

package com.samhalperin.phillybikesharemap.retrofit.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.samhalperin.phillybikesharemap.data.Station;


@Generated("org.jsonschema2pojo")
public class BikeData {
    private static final int LAT_INDEX = 1;
    private static final int LNG_INDEX = 0;


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

    public Station[] toStationArray() {
        List<Station> S = new ArrayList<Station>();
        for (Feature f : getFeatures()) {
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

}