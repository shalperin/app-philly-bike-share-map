package com.samhalperin.phillybikesharemap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.data.Station;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

public class MapsActivity extends ActionBarActivity implements Observer<Station>, OnMapReadyCallback {
    private GoogleMap mMap;
    private ClusterManager<Station> mClusterManager;
    private Tracker mTracker;
    private static final String SCREEN_NAME = "map_activity";
    private Subscription subscription;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        BikeShareApplication application = (BikeShareApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    public void onNext(Station station) {
        mClusterManager.addItem(station);
    }

    public void onCompleted() {
        mClusterManager.cluster();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        Toast.makeText(this, "Hello, Bike Share API?  This is Android calling... heh.. they hung up.", Toast.LENGTH_LONG).show();
    }

    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BikeShareApplication.PHILLY, BikeShareApplication.DEFAULT_ZOOM_LEVEL));
        mMap.setMyLocationEnabled(true);
        setUpClusterer();
        subscription = ((BikeShareApplication)getApplication()).getStationObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
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
                //TODO refreshStationData();
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
        mTracker.setScreenName(SCREEN_NAME);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mapFragment.getMapAsync(this);

    }


    private void setUpClusterer() {
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        StationClusterRenderer clusterRenderer = new StationClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(clusterRenderer);
    }

    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        super.onDestroy();
    }
}