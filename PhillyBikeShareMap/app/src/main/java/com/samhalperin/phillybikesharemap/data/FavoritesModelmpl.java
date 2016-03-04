package com.samhalperin.phillybikesharemap.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.samhalperin.phillybikesharemap.R;
import com.samhalperin.phillybikesharemap.data.FavoritesModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sqh on 3/2/16.
 */
public class FavoritesModelmpl implements FavoritesModel {
    private Context context;

    private Set<String> cache;
    private SharedPreferences prefs;

    public FavoritesModelmpl(Context context) {
        this.context = context;
        //init cache
        prefs = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                context.MODE_PRIVATE);
        cache = prefs.getStringSet(context.getString(R.string.favorites_pref), new HashSet<String>());
    }

    public Set<String> getKioskIds() {
        return cache;
    }

    public void addKioskId(String id) {
        cache.add(id);
        Set s = prefs.getStringSet(context.getString(R.string.favorites_pref), new HashSet<String>());
        s.add(id);
        prefs.edit().putStringSet(context.getString(R.string.favorites_pref), s).apply();

    }

    public boolean hasKioskId(String id) {
        return cache.contains(id);
    }

    public void deleteKioskId(String id) {
        cache.remove(id);
        Set s = prefs.getStringSet(context.getString(R.string.favorites_pref), new HashSet<String>());
        s.remove(id);
        prefs.edit().putStringSet(context.getString(R.string.favorites_pref),s).apply();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public int getCount() {
        return cache.size();
    }

    public String getPosition(int i) {
        //TODO
        // noooo.....
        // this is not a good way to make this thing stable.  refactor to not use shared prefs.
        List<String> l = new ArrayList<>();
        l.addAll(cache);
        Collections.sort(l);
        return l.get(i);
    }
}