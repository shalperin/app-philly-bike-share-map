package com.samhalperin.phillybikesharemap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.data.FavoritesModel;
import com.samhalperin.phillybikesharemap.data.FavoritesModelDBImpl;
import com.samhalperin.phillybikesharemap.data.FavoritesModelmpl;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.retrofit.Station;
import com.samhalperin.phillybikesharemap.retrofit.BikeClient;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private ClusterManager<Station> mClusterManager;
    private Tracker mTracker;
    private static final String SCREEN_NAME = "map_activity";
    private SupportMapFragment mapFragment;
    private BikeClient.Endpoints api;
    private FavoritesModel favoritesModel;
    StationClusterRenderer clusterRenderer;
    FavoritesModel model;
    private static final int ZOOM_IN_BY_ON_CLUSTER_CLICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);
        api = BikeClient.getApi(this);
        model = new FavoritesModelDBImpl(this);

        favoritesModel = new FavoritesModelDBImpl(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        BikeShareApplication application = (BikeShareApplication) getApplication();
        mTracker = application.getDefaultTracker();


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BikeShareApplication.PHILLY, BikeShareApplication.DEFAULT_ZOOM_LEVEL));
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);
        setUpClusterer();
        mMap.setOnMarkerClickListener(onMarkerClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_refresh:
                fetchData();
                return true;
            case R.id.action_attribution:
                intent = new Intent(this, AttributionActivity.class);
                startActivity(intent);
                break;
            case R.id.action_favorites:
                intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
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
        clusterRenderer = new StationClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(clusterRenderer);
        fetchData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void fetchData() {
        if (mClusterManager == null) {
            return;
        }
        mClusterManager.clearItems();
        findViewById(R.id.toolbar_progress_bar).setVisibility(View.VISIBLE);
        Call<BikeData> call = api.getBikeData();
        call.enqueue(new Callback<BikeData>() {
            @Override
            public void onResponse(Call<BikeData> call, Response<BikeData> response) {
                try {
                    BikeData data = response.body();
                    Station[] stations = data.asArray();
                    for(Station station : stations) {
                        if (mClusterManager != null) {
                            mClusterManager.addItem(station);
                        }
                    }
                    mClusterManager.cluster();
                    findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Ooops, sorry! Parse error.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<BikeData> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MapsActivity.this, "Ooops, sorry! Network error", Toast.LENGTH_LONG).show();
                findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
            }
        });
    }

    private GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker m) {
            String id =clusterRenderer.getMarkerIdMap().get(m.getId());
            if (favoritesModel.hasKioskId(id)) {
                favoritesModel.deleteKioskId(id);
                //Toast.makeText(MapsActivity.this, m.getTitle() + " removed from favorites.", Toast.LENGTH_LONG).show();
            } else {
                favoritesModel.addKioskId(id);
                //Toast.makeText(MapsActivity.this, m.getTitle() + " favorited!", Toast.LENGTH_LONG).show();
            }
            m.showInfoWindow();
        }
    };


    public boolean isMarkerFavorite(Marker marker) {
        String kioskId = clusterRenderer.getMarkerIdMap().get(marker.getId());
        return model.hasKioskId(kioskId);
    }

    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (isMarkerCluster(marker)) {
                float newZoom = mMap.getCameraPosition().zoom + ZOOM_IN_BY_ON_CLUSTER_CLICK ;
                LatLng newCenter= marker.getPosition();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newCenter,newZoom ));
                return true;
            } else {
                return false;
            }
        }
    };

    public boolean isMarkerCluster(Marker m) {
        //TODO WARNING: This is a pretty hacky way
        //of determining if a marker is a cluster, and we
        //call it in a couple of places.
        if (m.getTitle() == null) {
            // this might be a cluster.
            return true;
        } else {
            return false;
        }
    }
}
