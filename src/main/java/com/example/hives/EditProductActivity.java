package com.example.hives;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProductActivity extends AppCompatActivity {

    private EditText editProductName, editProductCategory, editProductPrice, editProductImageUrl;
    private Button updateButton;
    private FirebaseFirestore db;
    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        db = FirebaseFirestore.getInstance();
        editProductName = findViewById(R.id.editProductName);
        editProductCategory = findViewById(R.id.editProductCategory);
        editProductPrice = findViewById(R.id.editProductPrice);
        editProductImageUrl = findViewById(R.id.editProductImageUrl);
        updateButton = findViewById(R.id.updateButton);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("PRODUCT_ID");
            editProductName.setText(intent.getStringExtra("PRODUCT_NAME"));
            editProductCategory.setText(intent.getStringExtra("PRODUCT_CATEGORY"));
            editProductPrice.setText(String.valueOf(intent.getDoubleExtra("PRODUCT_PRICE", 0)));
            editProductImageUrl.setText(intent.getStringExtra("PRODUCT_IMAGE_URL"));
        }

        updateButton.setOnClickListener(v -> updateProduct());
    }

    private void updateProduct() {
        String name = editProductName.getText().toString();
        String category = editProductCategory.getText().toString();
        double price = Double.parseDouble(editProductPrice.getText().toString());
        String imageUrl = editProductImageUrl.getText().toString();

        db.collection("products").document(productId)
                .update("name", name, "category", category, "price", price, "imageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Notify success
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}
