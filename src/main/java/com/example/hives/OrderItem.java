package com.example.hives;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String category;   // Category of the item (e.g., cake, ice cream)
    private String imageUrl;   // URL to the image of the item
    private String name;       // Name of the item
    private double price;      // Price of one unit of the item
    private int quantity;      // Quantity of the item ordered
    private double totalPrice; // Total price for this item (price * quantity)

    // Default constructor required for calls to DataSnapshot.getValue(OrderItem.class)
    public OrderItem() {}

    // Parameterized constructor to initialize all fields
    public OrderItem(String category, String imageUrl, String name, double price, int quantity, double totalPrice) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and setters for all fields
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
