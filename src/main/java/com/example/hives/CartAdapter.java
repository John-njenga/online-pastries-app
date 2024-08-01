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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private OnCartItemChangedListener listener;

    public interface OnCartItemChangedListener {
        void onCartItemChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartItemChangedListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        holder.productName.setText(cartItem.getName());
        holder.productCategory.setText(cartItem.getCategory());
        holder.productPrice.setText(String.format("Kes %.2f", cartItem.getPrice() * cartItem.getQuantity()));
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Load the product image
        Glide.with(context).load(cartItem.getImageUrl()).into(holder.productImage);

        holder.incrementButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartItemChanged(); // Notify the activity
        });

        holder.decrementButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartItemChanged(); // Notify the activity
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            listener.onCartItemChanged(); // Notify the activity
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, incrementButton, decrementButton, removeButton;
        TextView productName, productCategory, productPrice, productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
