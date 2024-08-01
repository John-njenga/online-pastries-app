package com.example.hives;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Button addToCartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addToCartButton = findViewById(R.id.addToCartButton);  // Ensure this ID corresponds to a Button in the XML

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        fetchProductsFromFirestore();

        // Inside the onCreate method of OrderActivity
        addToCartButton.setOnClickListener(v -> {
            ArrayList<CartItem> cartItems = new ArrayList<>();
            for (Product product : productAdapter.getSelectedProducts()) {
                // Convert Product to CartItem
                CartItem cartItem = new CartItem(
                                        product.getName(),
                                        product.getCategory(),
                                        product.getPrice(),
                        1,
                        product.getImageUrl() // Default quantity
                                );
                cartItems.add(cartItem);
            }
            Intent intent = new Intent(OrderActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cartItems", cartItems);
            startActivity(intent);
        });
    }

        private void fetchProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }
}
