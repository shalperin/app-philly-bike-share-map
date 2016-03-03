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

import com.samhalperin.phillybikesharemap.FavoritesModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        api = BikeClient.getApi();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);
        model = new FavoritesModel(this);
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
            case R.id.action_map:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
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
                    FavoritesAdapter adapter = new FavoritesAdapter(FavoritesActivity.this,
                            model, data.asMap());
                    lv.setAdapter(adapter);
                    findViewById(R.id.toolbar_progress_bar).setVisibility(View.INVISIBLE);

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
}
