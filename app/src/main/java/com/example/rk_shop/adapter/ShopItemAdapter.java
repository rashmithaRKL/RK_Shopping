package com.example.rk_shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rk_shop.R;
import com.example.rk_shop.model.ShopItem;

import java.util.List;
import java.util.Locale;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder> {
    private List<ShopItem> shopItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ShopItem item);
    }

    public ShopItemAdapter(List<ShopItem> shopItems, OnItemClickListener listener) {
        this.shopItems = shopItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ShopItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemViewHolder holder, int position) {
        ShopItem item = shopItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public void updateItems(List<ShopItem> newItems) {
        this.shopItems = newItems;
        notifyDataSetChanged();
    }

    static class ShopItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView descriptionText;
        private TextView priceText;

        public ShopItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.itemName);
            descriptionText = itemView.findViewById(R.id.itemDescription);
            priceText = itemView.findViewById(R.id.itemPrice);
        }

        public void bind(final ShopItem item, final OnItemClickListener listener) {
            nameText.setText(item.getName());
            descriptionText.setText(item.getDescription());
            priceText.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
