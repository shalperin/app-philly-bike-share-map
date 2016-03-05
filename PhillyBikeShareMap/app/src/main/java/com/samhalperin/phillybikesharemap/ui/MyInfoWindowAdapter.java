package com.samhalperin.phillybikesharemap.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.samhalperin.phillybikesharemap.R;

/**
 * Created by sqh on 3/3/16.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    MapsActivity context;
    private static final String TAG = "MyInfoWindowAdapter";

    public MyInfoWindowAdapter(MapsActivity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (context.isMarkerCluster(marker)) {  // see note on this fn declaration.
            return null;
        }
        LayoutInflater inflater = context.getLayoutInflater();

        // This is a hack to support basic interactivity on the info window
        View view;
        if (context.isMarkerFavorite(marker)) {
            view = inflater.inflate(R.layout.info_window_favorite, null, false);
        } else {
            view = inflater.inflate(R.layout.info_window, null, false);
        }

        TextView snippet = (TextView)view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}
