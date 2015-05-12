package com.samhalperin.phillybikesharemap;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by sqh on 5/6/15.
 */
public class StationClusterRenderer extends DefaultClusterRenderer<Station> {

    private static BitmapDescriptor ACTIVE_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
    private static BitmapDescriptor SPECIAL_EVENT_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
    private static BitmapDescriptor PARTIAL_SERVICE_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
    private static BitmapDescriptor UNAVAILABLE_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
    private static BitmapDescriptor COMING_SOON_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private static BitmapDescriptor UNKNOWN_MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);


    public StationClusterRenderer(Context context, GoogleMap map,
                                  ClusterManager<Station> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Station station, MarkerOptions markerOptions) {
        markerOptions.snippet(station.getInfo());
        markerOptions.title(station.getStreet());
        if (station.getStatus() == Station.statuses.ACTIVE) {
            markerOptions.icon(ACTIVE_MARKER);
        } else if (station.getStatus() == Station.statuses.SPECIAL_EVENT){
            markerOptions.icon(SPECIAL_EVENT_MARKER);
        } else if (station.getStatus() == Station.statuses.PARTIAL_SERVICE){
            markerOptions.icon(PARTIAL_SERVICE_MARKER);
        }  else if (station.getStatus() == Station.statuses.COMING_SOON){
            markerOptions.icon(COMING_SOON_MARKER);
        }  else if (station.getStatus() == Station.statuses.UNAVAILABLE){
            markerOptions.icon(UNAVAILABLE_MARKER);
        } else if (station.getStatus() == Station.statuses.UNKNOWN){
            markerOptions.icon(UNKNOWN_MARKER);
        } else {
            markerOptions.icon(UNKNOWN_MARKER);
        }

        super.onBeforeClusterItemRendered(station, markerOptions);
    }




}
