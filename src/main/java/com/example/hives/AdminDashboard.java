package com.example.hives;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminProductAdapter productAdapter;
    private List<AdminProduct> productList;
    private FirebaseFirestore db;
    private Button addButton;
    private Button refreshButton;
    private Button viewOrdersButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new AdminProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, AddProductActivity.class);
            startActivity(intent);
        });

        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> fetchProducts());

        viewOrdersButton = findViewById(R.id.viewOrdersButton);
        viewOrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, ViewOrderActivity.class);
            startActivity(intent);
        });

        fetchProducts();
    }

    private void fetchProducts() {
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AdminProduct product = document.toObject(AdminProduct.class);
                            product.setId(document.getId());
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminDashboard.this, "Error getting products: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
