package com.example.examen_mov_unax_evalu2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_mov_unax_evalu2.R;
import com.example.examen_mov_unax_evalu2.Store;
import com.example.examen_mov_unax_evalu2.adapters.StoreAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class TiendasFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private Realm realm;
    private RealmResults<Store> stores;
    private OnTiendaInteractionListener listener;

    public interface OnTiendaInteractionListener {
        void onMapRequested(double lat, double lon, String name);
    }

    public TiendasFragment() {
        super(R.layout.fragment_tiendas);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTiendaInteractionListener) {
            listener = (OnTiendaInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTiendaInteractionListener");
        }
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
                if (listener != null) {
                    listener.onMapRequested(store.getLat(), store.getLon(), store.getName());
                }
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
        realm.beginTransaction();
        RealmResults<Store> allStores = realm.where(Store.class).findAll();
        for (Store s : allStores) {
            s.setActive(false);
        }
        selectedStore.setActive(true);
        realm.commitTransaction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}