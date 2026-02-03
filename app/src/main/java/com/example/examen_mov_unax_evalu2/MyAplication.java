package com.example.examen_mov_unax_evalu2;

import android.app.Application;
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
            // Insertar datos
            insertInitialData(realm);
        }
        realm.close();
    }

    private void insertInitialData(Realm realm) {
        List<Store> sampleStores = Utils.getSampleData();

        realm.executeTransaction(r -> {
            r.copyToRealmOrUpdate(sampleStores);
        });
    }
}