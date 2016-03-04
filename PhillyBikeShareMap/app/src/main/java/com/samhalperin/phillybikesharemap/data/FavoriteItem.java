package com.samhalperin.phillybikesharemap.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sqh on 3/4/16.
 */
public class FavoriteItem extends RealmObject {
    public static final String ID_FIELD_NAME = "id";

    @Required
    @PrimaryKey
    private String id;

    public FavoriteItem() {

    }

    public FavoriteItem(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
