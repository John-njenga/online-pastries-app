package com.example.hives;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private List<Product> selectedProducts = new ArrayList<>();

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productCategory.setText(product.getCategory());
        holder.productPrice.setText(String.format("Kes %.2f", product.getPrice()));
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedProducts.contains(product));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedProducts.add(product);
            } else {
                selectedProducts.remove(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ArrayList<Product> getSelectedProducts() {
        return new ArrayList<>(selectedProducts);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productCategory, productPrice;
        CheckBox checkBox;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            checkBox = itemView.findViewById(R.id.productCheckBox);
        }
    }
}
