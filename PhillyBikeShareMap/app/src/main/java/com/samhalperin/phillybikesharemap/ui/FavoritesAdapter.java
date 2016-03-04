package com.samhalperin.phillybikesharemap.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samhalperin.phillybikesharemap.data.FavoritesModelmpl;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.retrofit.Station;

import java.util.Map;

/**
 * Created by sqh on 3/2/16.
 */
public class FavoritesAdapter extends BaseAdapter  {
    Map<String, Station> stations;
    FavoritesModelmpl model;
    private Activity context;

    public FavoritesAdapter(Activity context, FavoritesModelmpl model, Map<String, Station> stations) {
        this.model = model;
        this.context = context;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return model.getCount();
    }

    @Override
    public Object getItem(int position) {
        String id = model.getPosition(position);
        try {
            return stations.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.favorite_list_item, parent, false);
        }
        Station station = (Station)getItem(position);
        if (station != null) {
            TextView address = (TextView) convertView.findViewById(R.id.address_street);
            TextView info = (TextView) convertView.findViewById(R.id.info);
            address.setText(station.getStreet());
            info.setText(station.getInfo());
        }



        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    public void remove(int position) {
        String id = model.getPosition(position);
        try {
            model.deleteKioskId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
