

package com.samhalperin.phillybikesharemap.retrofit.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import rx.Observable;
import rx.functions.Func1;

@Generated("org.jsonschema2pojo")
public class BikeData {

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

    public static final Func1<BikeData, Observable<Feature>> GET_FEATURES =
            new Func1<BikeData, Observable<Feature>>() {
                @Override
                public Observable call(BikeData bikeData) {
                    List<Feature> features = bikeData.getFeatures();
                    return Observable.from(features.toArray(new Feature[features.size()]));
                }};

}