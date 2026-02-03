package com.example.examen_mov_unax_evalu2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_mov_unax_evalu2.Item;
import com.example.examen_mov_unax_evalu2.R;

import java.util.List;
import java.util.Locale;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public SummaryAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
        return new SummaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView name, details, subtotal;
        CardView cardRoot;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            details = itemView.findViewById(R.id.tvDetails);
            subtotal = itemView.findViewById(R.id.tvSubtotal);
            cardRoot = itemView.findViewById(R.id.cardViewRoot);
        }

        public void bind(final Item item, final OnItemClickListener listener) {
            name.setText(item.getName());

            double sub = item.getPrice() * item.getQuantity();

            details.setText(String.format(Locale.getDefault(), "x%d - %.2f €", item.getQuantity(), item.getPrice()));
            subtotal.setText(String.format(Locale.getDefault(), "Subtotal: %.2f €", sub));

            // Cambio visual si está comprado se pone en metido en el carrito
            if (item.isPurchased()) {
                cardRoot.setCardBackgroundColor(Color.parseColor("#C8E6C9")); // Verde claro
            } else {
                cardRoot.setCardBackgroundColor(Color.WHITE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}