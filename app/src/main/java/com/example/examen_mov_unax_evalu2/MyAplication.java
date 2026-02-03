package com.example.examen_mov_unax_evalu2;

import android.app.Application;

import com.example.examen_mov_unax_evalu2.utils.Utils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.List;

public class MyAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("shopping.realm")
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(Store.class).count();

        if (count == 0) {
            insertInitialData(realm);
        }
        realm.close();
    }

    private void insertInitialData(Realm realm) {
        List<Store> sampleStores = Utils.getSampleData();
        realm.beginTransaction();
        Number maxId = realm.where(Store.class).max("id");
        int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

        for (Store store : sampleStores) {
            store.setId(nextId);
            realm.copyToRealm(store);
            nextId++;
        }
        realm.commitTransaction();
    }
}