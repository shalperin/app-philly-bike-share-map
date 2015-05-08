package com.samhalperin.phillybikesharemap;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by sqh on 5/6/15.
 */
public class StationClusterRenderer extends DefaultClusterRenderer<Station> {

    public StationClusterRenderer(Context context, GoogleMap map,
                                  ClusterManager<Station> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Station item, MarkerOptions markerOptions) {
        markerOptions.snippet(item.getStatus());
        markerOptions.title(item.getStreet());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }




}
