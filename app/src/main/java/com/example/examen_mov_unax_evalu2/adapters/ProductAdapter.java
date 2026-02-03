package com.example.examen_mov_unax_evalu2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_mov_unax_evalu2.Item;
import com.example.examen_mov_unax_evalu2.R;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Item> productList;
    private OnProductAction listener;

    // Interfaz para avisar al Fragment cuando se pulsa + o -
    public interface OnProductAction {
        void onPlusClick(Item item);
        void onMinusClick(Item item);
    }

    public ProductAdapter(List<Item> productList, OnProductAction listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Item item = productList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, price, quantity;
        Button btnPlus, btnMinus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            category = itemView.findViewById(R.id.tvProductCategory);
            price = itemView.findViewById(R.id.tvProductPrice);
            quantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }

        public void bind(final Item item, final OnProductAction listener) {
            name.setText(item.getName());
            category.setText(item.getCategory());
            price.setText(String.format(Locale.getDefault(), "%.2f €", item.getPrice()));
            quantity.setText(String.valueOf(item.getQuantity()));

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlusClick(item);
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMinusClick(item);
                }
            });
        }
    }
}