package com.example.examen_mov_unax_evalu2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class TiendasFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private Realm realm;
    private RealmResults<Store> stores;

    public TiendasFragment() {
        super(R.layout.fragment_tiendas);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvTiendas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stores = realm.where(Store.class).findAll();

        adapter = new StoreAdapter(stores, new StoreAdapter.OnStoreActionListener() {
            @Override
            public void onStoreClick(Store store) {
                setActiveStore(store);
            }

            @Override
            public void onStoreLongClick(Store store) {
                openMap(store);
            }
        });

        recyclerView.setAdapter(adapter);

        stores.addChangeListener(new RealmChangeListener<RealmResults<Store>>() {
            @Override
            public void onChange(RealmResults<Store> stores) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setActiveStore(Store selectedStore) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Store> allStores = realm.where(Store.class).findAll();
                for (Store s : allStores) {
                    s.setActive(false);
                }
                selectedStore.setActive(true);
            }
        });
    }

    private void openMap(Store store) {
        String mapUri = Utils.openStoreInMaps(store);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}