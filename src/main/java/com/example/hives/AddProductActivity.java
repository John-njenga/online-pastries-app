package com.example.hives;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProductActivity extends AppCompatActivity {

    private EditText nameEditText, categoryEditText, priceEditText, imageUrlEditText;
    private Button addButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameEditText = findViewById(R.id.nameEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        priceEditText = findViewById(R.id.priceEditText);
        imageUrlEditText = findViewById(R.id.imageUrlEditText);
        addButton = findViewById(R.id.addButton);

        db = FirebaseFirestore.getInstance();

        addButton.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = nameEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminProduct product = new AdminProduct();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_LONG).show();
                    finish();  // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
