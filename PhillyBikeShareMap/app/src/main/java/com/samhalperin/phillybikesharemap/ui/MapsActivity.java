package com.samhalperin.phillybikesharemap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.samhalperin.phillybikesharemap.Constants;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.data.Station;
import com.samhalperin.phillybikesharemap.data.StationDataTask;

public class MapsActivity extends ActionBarActivity implements StationDataTask.StationDataLoader {
    private GoogleMap mMap;
    private ClusterManager<Station> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refreshStationData();
                return true;
            case R.id.action_attribution:
                Intent intent = new Intent(this, AttributionActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

      private void setUpMapIfNeeded() {
          if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.PHILLY, Constants.DEFAULT_ZOOM_LEVEL));
        mMap.setMyLocationEnabled(true);
        setUpClusterer();
        refreshStationData();
    }

    private void refreshStationData() {
        StationDataTask task = new StationDataTask(this);
        task.execute(getString(R.string.api_url));
    }

    private void setUpClusterer() {
        mClusterManager = new ClusterManager<>(this, mMap);

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        StationClusterRenderer clusterRenderer = new StationClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(clusterRenderer);
    }

    // StationDataTask.StationDataLoader interface
    @Override
    public void loadStationData(Station[] stations) {
        mClusterManager.clearItems();
        for (Station s : stations) {
            mClusterManager.addItem(s);
        }
        mClusterManager.cluster();
    }
}