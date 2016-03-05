package com.samhalperin.phillybikesharemap.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.samhalperin.phillybikesharemap.data.FavoritesModel;
import com.samhalperin.phillybikesharemap.data.FavoritesModelDBImpl;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.rest.BikeClient;
import com.samhalperin.phillybikesharemap.rest.Station;
import com.samhalperin.phillybikesharemap.rest.pojo.BikeData;

import java.util.Map;

public class FavoritesActivity extends AppCompatActivity implements BikeClient.BikeClientResponseHandler {

    ListView lv;
    private BikeClient api;
    FavoritesModel model;
    FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(null);

        model = new FavoritesModelDBImpl(this);
        lv = (ListView)findViewById(R.id.favorites_lv);
        lv.setEmptyView(findViewById(R.id.empty_view));

        api = new BikeClient(this);
        api.setResponseHandler(this);
        api.fetch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBikeApiFetchSuccess(BikeData bikedata) {
        Map<String, Station> map;
        try {
           map = bikedata.asMap();
        } catch (BikeData.ParseException e) {
            Toast.makeText(this, "Ooops, sorry! Parse error.", Toast.LENGTH_LONG).show();
            return;
        }

        adapter = new FavoritesAdapter(FavoritesActivity.this,
                model, map);
        lv.setAdapter(adapter);
        findViewById(R.id.loading_view).setVisibility(View.GONE);
        findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
        bindSwipeListener();
    }

    @Override
    public void onBikeApiFetchFailure(String msg) {
        Toast.makeText(FavoritesActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    //https://github.com/romannurik/Android-SwipeToDismiss/blob/master/src/com/example/android/swipedismiss/MainActivity.java
    private void bindSwipeListener() {
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lv,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                                Toast.makeText(FavoritesActivity.this, "Favorite deleted", Toast.LENGTH_LONG).show();
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());

    }
}
