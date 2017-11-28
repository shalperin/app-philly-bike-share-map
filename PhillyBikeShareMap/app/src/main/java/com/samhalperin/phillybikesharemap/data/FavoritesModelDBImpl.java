package com.samhalperin.phillybikesharemap.data;

import android.content.Context;

import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by sqh on 3/4/16.
 */
public class FavoritesModelDBImpl implements FavoritesModel{

    //TODO :  doing realm tx on the UI thread.  going to see if I can get away with this.

    private Realm realm;
    private RealmConfiguration realmConfig;

    public FavoritesModelDBImpl(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void addKioskId(String id) {
        FavoriteItem item = new FavoriteItem(id);
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();
    }

    @Override
    public boolean hasKioskId(String id) {
        FavoriteItem item = realm.where(FavoriteItem.class).equalTo(FavoriteItem.ID_FIELD_NAME, id).findFirst();
        return !(item == null);
    }

    @Override
    public void deleteKioskId(String id) {
        realm.beginTransaction();
        FavoriteItem item = realm.where(FavoriteItem.class).equalTo(FavoriteItem.ID_FIELD_NAME, id).findFirst();
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public int getCount() {
        RealmResults<FavoriteItem> ids = realm.where(FavoriteItem.class).findAll();
        return ids.size();
    }

    @Override
    public String getPosition(int i) {
        RealmResults<FavoriteItem> ids = realm.where(FavoriteItem.class).findAll();
        return ids.get(i).getId();
    }
}
