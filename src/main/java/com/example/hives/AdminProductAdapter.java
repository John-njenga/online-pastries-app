package com.example.hives;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder> {

    private Context context;
    private List<AdminProduct> productList;
    private FirebaseFirestore db;

    public AdminProductAdapter(Context context, List<AdminProduct> productList) {
        this.context = context;
        this.productList = productList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        AdminProduct product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productCategory.setText(product.getCategory());
        holder.productPrice.setText(String.format("Kes %.2f", product.getPrice()));
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            intent.putExtra("PRODUCT_NAME", product.getName());
            intent.putExtra("PRODUCT_CATEGORY", product.getCategory());
            intent.putExtra("PRODUCT_PRICE", product.getPrice());
            intent.putExtra("PRODUCT_IMAGE_URL", product.getImageUrl());
            context.startActivity(intent);
        });
        holder.deleteButton.setOnClickListener(v -> {
            db.collection("products").document(product.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        productList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class AdminProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productCategory, productPrice;
        Button editButton, deleteButton;

        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
