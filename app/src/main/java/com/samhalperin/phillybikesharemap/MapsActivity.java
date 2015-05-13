package com.samhalperin.phillybikesharemap;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends ActionBarActivity implements StationDataTask.StationDataLoader {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private StationClusterRenderer mClusterRenderer;
    private ClusterManager<Station> mClusterManager;

    public static final LatLng PHILLY = new LatLng(39.9500, -75.1667);
    private static final int DEFAULT_ZOOM_LEVEL = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refreshStationData();
            return true;
        } else if (id == R.id.action_attribution) {
            Intent intent = new Intent(this, AttributionActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

      private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
          if (mMap == null) {
              // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PHILLY, DEFAULT_ZOOM_LEVEL));
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

        mClusterRenderer = new StationClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(mClusterRenderer);
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
