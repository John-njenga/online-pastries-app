package com.example.hives;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView userNameTextView, userEmailTextView, userPhoneTextView, orderSummaryTextView, totalAmountTextView;
    private EditText addressEditText;
    private Button proceedToPaymentButton;

    private DatabaseReference deliveryReference;
    private String accessToken;
    private String orderSummary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPhoneTextView = findViewById(R.id.userPhoneTextView);
        orderSummaryTextView = findViewById(R.id.orderSummaryTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        addressEditText = findViewById(R.id.addressEditText);
        proceedToPaymentButton = findViewById(R.id.proceedToPaymentButton);

        // Initialize Firebase Database reference
        deliveryReference = FirebaseDatabase.getInstance().getReference("delivery");

        // Load user info from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "N/A");
        String userEmail = sharedPreferences.getString("userEmail", "N/A");
        String userPhone = sharedPreferences.getString("userPhone", "N/A");

        userNameTextView.setText(String.format("Name: %s", userName));
        userEmailTextView.setText(String.format("Email: %s", userEmail));
        userPhoneTextView.setText(String.format("Phone: %s", userPhone));

        // Get cart items and total amount from intent
        ArrayList<CartItem> cartItems = null;
        double totalAmount = 0.0;
        if (getIntent() != null && getIntent().hasExtra("cartItems") && getIntent().hasExtra("totalAmount")) {
            cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");
            totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

            // Display order summary and total amount
            StringBuilder orderSummaryBuilder = new StringBuilder();
            for (CartItem item : cartItems) {
                orderSummaryBuilder.append(item.getName()).append(" - ").append(item.getQuantity()).append(" x Kes ").append(item.getPrice()).append("\n");
            }
            orderSummary = orderSummaryBuilder.toString();
            orderSummaryTextView.setText(orderSummary);
            totalAmountTextView.setText(String.format("Total: Kes %.2f", totalAmount));
        }

        double finalTotalAmount = totalAmount; // make totalAmount final
        ArrayList<CartItem> finalCartItems = cartItems; // make cartItems final
        proceedToPaymentButton.setOnClickListener(v -> {
            String address = addressEditText.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(OrderSummaryActivity.this, "Please enter your address.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save delivery info to Firebase
            String deliveryId = deliveryReference.push().getKey();
            if (deliveryId != null) {
                DeliveryInfo deliveryInfo = new DeliveryInfo(userName, userEmail, userPhone, address, finalTotalAmount, finalCartItems);
                deliveryReference.child(deliveryId).setValue(deliveryInfo);

                // Get access token and perform STK Push using Daraja API
                getAccessToken(userPhone, finalTotalAmount, orderSummary);
            }
        });
    }

    private void getAccessToken(String phoneNumber, double amount, String orderSummary) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("Authorization", "Basic " + android.util.Base64.encodeToString(
                        ("D1kYaYH8Ml2Ny6vMrUH4bNRwteUr0LivPOGeVAnSk53mUCGK:XjS2Lo3uNtE3IqA9rckAN6Dq7R2VbodB43te1myCu04QvuQjejAtoyh3uoOYGAtW").getBytes(),
                        android.util.Base64.NO_WRAP))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(OrderSummaryActivity.this, "Failed to get access token", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        accessToken = jsonObject.getString("access_token");
                        performStkPush(phoneNumber, amount, orderSummary);
                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(OrderSummaryActivity.this, "Failed to parse access token", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(OrderSummaryActivity.this, "Failed to get access token", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void performStkPush(String phoneNumber, double amount, String orderSummary) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String password = android.util.Base64.encodeToString(
                ("174379" + "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919" + timestamp).getBytes(),
                android.util.Base64.NO_WRAP
        );

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("BusinessShortCode", "174379");
            jsonObject.put("Password", password);
            jsonObject.put("Timestamp", timestamp);
            jsonObject.put("TransactionType", "CustomerPayBillOnline");
            jsonObject.put("Amount", amount);
            jsonObject.put("PartyA", phoneNumber);
            jsonObject.put("PartyB", "174379");
            jsonObject.put("PhoneNumber", phoneNumber);
            jsonObject.put("CallBackURL", "https://yourdomain.com/path");
            jsonObject.put("AccountReference", "Hives Pastries");
            jsonObject.put("TransactionDesc", "Order");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(OrderSummaryActivity.this, "Failed to initiate STK Push", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderSummaryActivity.this, "STK Push initiated", Toast.LENGTH_SHORT).show();
                        showOrderConfirmationDialog();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(OrderSummaryActivity.this, "Failed to initiate STK Push", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void showOrderConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Order Received")
                .setMessage("Thank you for your order with Hives Pastries. \n Your order has been received and will be delivered soon.")
                .setPositiveButton("OK", (dialog, which) -> navigateToUserDashboard())
                .setCancelable(false)
                .show();
    }

    private void navigateToUserDashboard() {
        Intent intent = new Intent(OrderSummaryActivity.this, UserDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
