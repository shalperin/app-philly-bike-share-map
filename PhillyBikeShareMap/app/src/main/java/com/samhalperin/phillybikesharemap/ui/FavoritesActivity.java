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
import com.samhalperin.phillybikesharemap.data.FavoritesModelmpl;
import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.retrofit.BikeClient;
import com.samhalperin.phillybikesharemap.retrofit.pojo.BikeData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    ListView lv;
    private BikeClient.Endpoints api;
    FavoritesModel model;
    FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        api = BikeClient.getApi(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);
        model = new FavoritesModelDBImpl(this);
        lv = (ListView)findViewById(R.id.favorites_lv);
        lv.setEmptyView(findViewById(R.id.empty_view));
        fetchData();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(null);


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
                fetchData();
                return true;
            case R.id.action_attribution:
                intent = new Intent(this, AttributionActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        findViewById(R.id.toolbar_progress_bar).setVisibility(View.VISIBLE);
        Call<BikeData> call = api.getBikeData();
        call.enqueue(new Callback<BikeData>() {
            @Override
            public void onResponse(Call<BikeData> call, Response<BikeData> response) {
                try {
                    BikeData data = response.body();
                    // I am conflicted about blowing away the adapter here and recreating
                    // it, but I definitely don't want to init it with null for the Stations
                    // Map.
                    adapter = new FavoritesAdapter(FavoritesActivity.this,
                            model, data.asMap());
                    lv.setAdapter(adapter);
                    findViewById(R.id.loading_view).setVisibility(View.GONE);
                    findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
                    bindSwipeListener();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FavoritesActivity.this, "Ooops, sorry! Parse error.", Toast.LENGTH_LONG).show();
                    findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BikeData> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(FavoritesActivity.this, "Ooops, sorry! Network error", Toast.LENGTH_LONG).show();
            }
        });
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
