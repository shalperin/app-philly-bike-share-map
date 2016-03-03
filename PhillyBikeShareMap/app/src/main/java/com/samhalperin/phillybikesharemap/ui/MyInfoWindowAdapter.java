package com.samhalperin.phillybikesharemap.ui;

import android.app.Activity;
import android.content.Context;
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
    Activity context;

    public MyInfoWindowAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.info_window, null, false);
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
