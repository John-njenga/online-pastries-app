package com.example.hives;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangedListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView totalAmountTextView;
    private Button proceedToCheckoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        proceedToCheckoutButton = findViewById(R.id.proceedToCheckoutButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, this); // Pass this as the listener
        recyclerView.setAdapter(cartAdapter);

        // Get the cart items passed via intent
        if (getIntent() != null && getIntent().hasExtra("cartItems")) {
            cartItemList.addAll(getIntent().getParcelableArrayListExtra("cartItems"));
            cartAdapter.notifyDataSetChanged();
            updateTotalAmount();
        }

        proceedToCheckoutButton.setOnClickListener(v -> {
            // Save items to Firebase Realtime Database on checkout
            // Ensure the cart items are correctly saved and passed to Checkout activity
            Intent intent = new Intent(CartActivity.this, Checkout.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItemList));
            intent.putExtra("totalAmount", calculateTotalAmount());
            startActivity(intent);
        });
    }

    @Override
    public void onCartItemChanged() {
        updateTotalAmount(); // Update total amount whenever an item changes
    }

    private void updateTotalAmount() {
        double totalAmount = calculateTotalAmount();
        totalAmountTextView.setText(String.format("Total: Kes %.2f", totalAmount));
    }

    private double calculateTotalAmount() {
        double total = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        return total;
    }
}
