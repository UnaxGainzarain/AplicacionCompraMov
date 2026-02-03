package com.example.examen_mov_unax_evalu2.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_mov_unax_evalu2.Item;
import com.example.examen_mov_unax_evalu2.adapters.ProductAdapter;
import com.example.examen_mov_unax_evalu2.R;
import com.example.examen_mov_unax_evalu2.Store;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class ListaFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvTitle;
    private ProductAdapter adapter;
    private Realm realm;
    private Store activeStore;

    // Escuchamos cambios por si cambiamos de tienda activa en la otra pestaña
    private final RealmChangeListener<Store> storeChangeListener = new RealmChangeListener<Store>() {
        @Override
        public void onChange(Store store) {
            if (store.isValid() && store.isActive()) {
                updateUI(store);
            } else {
                loadActiveStore();
            }
        }
    };

    public ListaFragment() {
        super(R.layout.fragment_lista);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tvActiveStoreTitle);
        recyclerView = view.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActiveStore();
    }

    private void loadActiveStore() {
        // Buscamos la tienda marcada como activa
        activeStore = realm.where(Store.class).equalTo("isActive", true).findFirst();

        if (activeStore != null) {
            activeStore.addChangeListener(storeChangeListener);
            updateUI(activeStore);
        } else {
            tvTitle.setText("Selecciona una tienda primero");
            recyclerView.setAdapter(null);
        }
    }

    private void updateUI(Store store) {
        tvTitle.setText("Tienda activa: " + store.getName());
        RealmList<Item> items = store.getItems();

        // Creamos el adaptador y definimos qué pasa al pulsar + o -
        adapter = new ProductAdapter(items, new ProductAdapter.OnProductAction() {
            @Override
            public void onPlusClick(final Item item) {
                realm.executeTransaction(r -> item.setQuantity(item.getQuantity() + 1));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMinusClick(final Item item) {
                realm.executeTransaction(r -> {
                    if (item.getQuantity() > 0) item.setQuantity(item.getQuantity() - 1);
                });
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (activeStore != null) activeStore.removeChangeListener(storeChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}