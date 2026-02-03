package com.example.examen_mov_unax_evalu2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> storeList;
    private OnStoreActionListener listener;

    public interface OnStoreActionListener {
        void onStoreClick(Store store);
        void onStoreLongClick(Store store);
    }

    public StoreAdapter(List<Store> storeList, OnStoreActionListener listener) {
        this.storeList = storeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.bind(store, listener);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress, tvLatLon, tvActiveBadge;
        LinearLayout layoutRoot;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStoreName);
            tvAddress = itemView.findViewById(R.id.tvStoreAddress);
            tvLatLon = itemView.findViewById(R.id.tvStoreLatLon);
            tvActiveBadge = itemView.findViewById(R.id.tvActiveBadge);
            layoutRoot = itemView.findViewById(R.id.layoutStore);
        }

        public void bind(final Store store, final OnStoreActionListener listener) {
            tvName.setText(store.getName());
            tvAddress.setText(store.getAddress());
            tvLatLon.setText("Lat: " + store.getLat() + " Lon: " + store.getLon());

            if (store.isActive()) {
                tvActiveBadge.setVisibility(View.VISIBLE);
                layoutRoot.setBackgroundColor(0xFFE1BEE7);
            } else {
                tvActiveBadge.setVisibility(View.GONE);
                layoutRoot.setBackgroundColor(0xFFFFFFFF);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStoreClick(store);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onStoreLongClick(store);
                    return true;
                }
            });
        }
    }
}