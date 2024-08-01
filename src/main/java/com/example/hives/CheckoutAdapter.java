package com.example.hives;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CheckoutAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.itemName.setText(cartItem.getName());
        holder.itemCategory.setText(cartItem.getCategory());
        holder.itemPrice.setText(String.format("Kes %.2f", cartItem.getPrice()));
        holder.itemQuantity.setText(String.format("Qty: %d", cartItem.getQuantity()));
        holder.itemTotalPrice.setText(String.format("Kes %.2f", cartItem.getTotalPrice()));

        // Load the product image
        Glide.with(context)
                .load(cartItem.getImageUrl())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName, itemCategory, itemPrice, itemQuantity, itemTotalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemTotalPrice = itemView.findViewById(R.id.itemTotalPrice);
        }
    }
}
