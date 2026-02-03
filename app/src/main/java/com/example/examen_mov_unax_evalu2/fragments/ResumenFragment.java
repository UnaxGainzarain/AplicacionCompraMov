package com.example.examen_mov_unax_evalu2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_mov_unax_evalu2.Item;
import com.example.examen_mov_unax_evalu2.MainActivity;
import com.example.examen_mov_unax_evalu2.R;
import com.example.examen_mov_unax_evalu2.Store;
import com.example.examen_mov_unax_evalu2.adapters.SummaryAdapter;
import com.example.examen_mov_unax_evalu2.utils.Utils;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ResumenFragment extends Fragment {

    private TextView tvStoreTitle, tvTotalInfo;
    private RecyclerView recyclerView;
    private Button btnShare, btnClear;

    private Realm realm;
    private Store activeStore;
    private RealmResults<Item> cartItems; // Items con cantidad > 0
    private SummaryAdapter adapter;

    public ResumenFragment() {
        super(R.layout.fragment_resumen);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvStoreTitle = view.findViewById(R.id.tvStoreTitle);
        tvTotalInfo = view.findViewById(R.id.tvTotalInfo);
        recyclerView = view.findViewById(R.id.rvSummary);
        btnShare = view.findViewById(R.id.btnShare);
        btnClear = view.findViewById(R.id.btnClear);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Acciones de botones
        btnShare.setOnClickListener(v -> shareList());
        btnClear.setOnClickListener(v -> clearList());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        activeStore = realm.where(Store.class).equalTo("isActive", true).findFirst();

        if (activeStore != null) {
            tvStoreTitle.setText("Tienda activa: " + activeStore.getName());

            // Filtramos SOLO los productos con cantidad > 0 de esta tienda
            cartItems = activeStore.getItems().where().greaterThan("quantity", 0).findAll();

            adapter = new SummaryAdapter(cartItems, new SummaryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final Item item) {
                    // Al hacer click, cambiamos el estado de "purchased" (marcado/desmarcado)
                    realm.executeTransaction(r -> item.setPurchased(!item.isPurchased()));
                    adapter.notifyDataSetChanged();
                }
            });
            recyclerView.setAdapter(adapter);

            cartItems.addChangeListener(new RealmChangeListener<RealmResults<Item>>() {
                @Override
                public void onChange(RealmResults<Item> items) {
                    calculateTotals(items);
                    adapter.notifyDataSetChanged();
                }
            });

            // Calculo inicial
            calculateTotals(cartItems);

        } else {
            tvStoreTitle.setText("No hay tienda activa");
            tvTotalInfo.setText("");
            recyclerView.setAdapter(null);
        }
    }

    private void calculateTotals(RealmResults<Item> items) {
        int totalProducts = items.size();
        int totalUnits = 0;
        double totalPrice = 0.0;

        for (Item item : items) {
            totalUnits += item.getQuantity();
            totalPrice += (item.getPrice() * item.getQuantity());
        }

        String info = String.format(Locale.getDefault(),
                "Productos añadidos: %d\nUnidades totales: %d\nTotal: %.2f €",
                totalProducts, totalUnits, totalPrice);
        tvTotalInfo.setText(info);
    }

    private void clearList() {
        if (activeStore != null) {
            realm.executeTransaction(r -> {
                for (Item item : activeStore.getItems()) {
                    item.setQuantity(0);
                    item.setPurchased(false);
                }
            });
            Toast.makeText(getContext(), "Lista limpiada", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareList() {
        if (activeStore == null || cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(getContext(), "La lista está vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailBody = Utils.buildShoppingListEmailBody(activeStore, realm.copyFromRealm(cartItems));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(android.net.Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Lista de compra: " + activeStore.getName());
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Intent intentGenerico = new Intent(Intent.ACTION_SEND);
            intentGenerico.setType("text/plain");
            intentGenerico.putExtra(Intent.EXTRA_SUBJECT, "Lista de compra: " + activeStore.getName());
            intentGenerico.putExtra(Intent.EXTRA_TEXT, emailBody);
            startActivity(Intent.createChooser(intentGenerico, "Enviar lista por..."));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}