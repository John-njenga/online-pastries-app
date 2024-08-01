package com.example.hives;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewOrderActivity extends AppCompatActivity {

    private static final String TAG = "ViewOrderActivity";

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<OrderDetails> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        // Initialize RecyclerView
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        orders = new ArrayList<>();
        orderAdapter = new OrderAdapter(orders);
        recyclerViewOrders.setAdapter(orderAdapter);

        // Fetch all orders from Firebase
        fetchAllOrders();
    }

    private void fetchAllOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("delivery");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    orders.clear();
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        String userName = orderSnapshot.child("userName").getValue(String.class);
                        String userEmail = orderSnapshot.child("userEmail").getValue(String.class);
                        String userPhone = orderSnapshot.child("userPhone").getValue(String.class);
                        String address = orderSnapshot.child("address").getValue(String.class);
                        Double totalAmountObj = orderSnapshot.child("totalAmount").getValue(Double.class);
                        double totalAmount = (totalAmountObj != null) ? totalAmountObj : 0.0;

                        List<OrderItem> orderItems = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : orderSnapshot.child("cartItems").getChildren()) {
                            OrderItem item = itemSnapshot.getValue(OrderItem.class);
                            if (item != null) {
                                orderItems.add(item);
                            } else {
                                Log.e(TAG, "Error parsing OrderItem");
                            }
                        }

                        // Create OrderDetails object
                        OrderDetails orderDetails = new OrderDetails(
                                userName,
                                userEmail,
                                userPhone,
                                address,
                                totalAmount,
                                orderItems
                        );

                        orders.add(orderDetails);
                    }
                    orderAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "Error loading orders", e);
                    Toast.makeText(ViewOrderActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(ViewOrderActivity.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
