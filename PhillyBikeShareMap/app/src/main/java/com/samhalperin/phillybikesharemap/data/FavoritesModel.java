package com.samhalperin.phillybikesharemap.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sqh on 3/4/16.
 */
public interface FavoritesModel {

    Set<String> getKioskIds();

    void addKioskId(String id);

    boolean hasKioskId(String id);

    void deleteKioskId(String id);

    boolean isEmpty();

    int getCount();

    String getPosition(int i);

}
