package com.samhalperin.phillybikesharemap.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.samhalperin.phillybikesharemap.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.ab_icon);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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
            case R.id.action_map:
                Intent intent2 = new Intent(this, MapsActivity.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }
}
