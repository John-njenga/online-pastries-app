package com.example.hives;

import java.io.Serializable;
import java.util.List;

public class OrderDetails implements Serializable {
    private String userName;       // Name of the user who placed the order
    private String userEmail;      // Email of the user
    private String userPhone;      // Phone number of the user
    private String address;        // Delivery address
    private double totalAmount;    // Total amount for the order
    private List<OrderItem> orderItems; // List of items in the order

    // Default constructor required for calls to DataSnapshot.getValue(OrderDetails.class)
    public OrderDetails() {}

    // Parameterized constructor to initialize all fields
    public OrderDetails(String userName, String userEmail, String userPhone, String address, double totalAmount, List<OrderItem> orderItems) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.address = address;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    // Getters and setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
