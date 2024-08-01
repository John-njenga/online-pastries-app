package com.example.hives;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private RecyclerView checkoutRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private List<CartItem> checkoutItemList;
    private TextView checkoutTotalAmount;
    private Button confirmOrderButton;
    private DatabaseReference ordersReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutRecyclerView = findViewById(R.id.checkoutRecyclerView);
        checkoutTotalAmount = findViewById(R.id.checkoutTotalAmount);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);

        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkoutItemList = new ArrayList<>();
        checkoutAdapter = new CheckoutAdapter(this, checkoutItemList);
        checkoutRecyclerView.setAdapter(checkoutAdapter);

        // Get cart items and total amount passed via intent
        if (getIntent() != null && getIntent().hasExtra("cartItems") && getIntent().hasExtra("totalAmount")) {
            checkoutItemList.addAll((ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems"));
            checkoutAdapter.notifyDataSetChanged();

            double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
            checkoutTotalAmount.setText(String.format("Total: Kes %.2f", totalAmount));
        }

        // Initialize Firebase Database reference
        ordersReference = FirebaseDatabase.getInstance().getReference("orders");

        confirmOrderButton.setOnClickListener(v -> {
            // Save order to Firebase
            saveOrderToFirebase();

            // Start OrderSummaryActivity with cart items and total amount
            Intent intent = new Intent(Checkout.this, OrderSummaryActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(checkoutItemList));
            intent.putExtra("totalAmount", getIntent().getDoubleExtra("totalAmount", 0.0));
            startActivity(intent);

            // Finish the activity
            finish();
        });
    }

    private void saveOrderToFirebase() {
        String orderId = ordersReference.push().getKey();
        if (orderId != null) {
            ordersReference.child(orderId).setValue(checkoutItemList);
        }
    }
}
