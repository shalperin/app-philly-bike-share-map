package com.samhalperin.phillybikesharemap.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.samhalperin.phillybikesharemap.BikeShareApplication;
import com.samhalperin.phillybikesharemap.data.FavoritesModel;
import com.samhalperin.phillybikesharemap.data.FavoritesModelDBImpl;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.rest.Station;
import com.samhalperin.phillybikesharemap.rest.BikeClient;
import com.samhalperin.phillybikesharemap.rest.pojo.BikeData;

import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener,
        BikeClient.BikeClientResponseHandler
{
    private static final String TAG = "MapsActivity";
    private static final int PERMISSION_RQ_CODE = 1;
    private static final String SCREEN_NAME = "map_activity";
    private static final int ZOOM_IN_BY_ON_CLUSTER_CLICK = 1;


    private GoogleMap mMap;
    private ClusterManager<Station> mClusterManager;
    private Tracker mTracker;
    private SupportMapFragment mapFragment;
    private BikeClient api;
    private FavoritesModel favoritesModel;
    private GoogleApiClient mGoogleApiClient;
    private StationClusterRenderer clusterRenderer;
    private FavoritesModel model;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);
        api = new BikeClient(this);
        api.setResponseHandler(this);

        model = new FavoritesModelDBImpl(this);

        favoritesModel = new FavoritesModelDBImpl(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        BikeShareApplication application = (BikeShareApplication) getApplication();
        mTracker = application.getDefaultTracker();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BikeShareApplication.PHILLY, BikeShareApplication.DEFAULT_ZOOM_LEVEL));
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(this);

        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<>(this, mMap);
            mMap.setOnCameraChangeListener(mClusterManager);
            clusterRenderer = new StationClusterRenderer(this, mMap, mClusterManager);
            mClusterManager.setRenderer(clusterRenderer);
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        //mGoogleApiClient.connect();
        api.fetch();
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
                findViewById(R.id.toolbar_progress_bar).setVisibility(View.VISIBLE);
                api.fetch();
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
        mGoogleApiClient.reconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    @Override
    public void onBikeApiFetchSuccess(BikeData bikedata) {
        List<Station> stations;
        try {
            stations = bikedata.asList();
        } catch (BikeData.ParseException e) {
            Toast.makeText(MapsActivity.this, "Ooops! parse error.", Toast.LENGTH_LONG).show();
            return;
        }

        mClusterManager.clearItems();
        for(Station station : stations) {
            if (mClusterManager != null) {
                mClusterManager.addItem(station);
            }
        }
        mClusterManager.cluster();
        findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBikeApiFetchFailure(String message) {
        Toast.makeText(MapsActivity.this, message, Toast.LENGTH_LONG).show();
        findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
    }


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


    public boolean isMarkerFavorite(Marker marker) {
        String kioskId = clusterRenderer.getMarkerIdMap().get(marker.getId());
        return model.hasKioskId(kioskId);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (isMarkerCluster(marker)) {
            float newZoom = mMap.getCameraPosition().zoom + ZOOM_IN_BY_ON_CLUSTER_CLICK ;
            LatLng newCenter= marker.getPosition();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newCenter,newZoom ));
            return true;
        } else {
            return false;
        }
    }

    public boolean isMarkerCluster(Marker m) {
        /* TODO WARNING: This is a pretty hacky way of determining if a marker is a cluster
        * encapsulating the calls that use this logic here so at least it will only be broken
        * in one place if something changes in the future.
        * */
        return m.getTitle() == null;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                float bearing = mMap.getCameraPosition().bearing;
                CameraPosition newCameraPosition = new CameraPosition(
                        new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                        BikeShareApplication.STREET_LEVEL_ZOOM, 0, bearing);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
            } else {
                Log.e(TAG, "location was null");
            }
        } else {
            // if they somehow revoked the permission the button ui should be
            // removed on startup.
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        //todo ??
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //register for location updates.
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            LocationRequest lr = new LocationRequest();
            lr.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            lr.setFastestInterval(BikeShareApplication.LOCATION_UPDATE_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, lr, this);

        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_RQ_CODE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient.reconnect();
                } else {
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
                return;
            }
        }
    }


}
