package com.example.hives;

import java.io.Serializable;
import java.util.List;

public class DeliveryInfo implements Serializable {
    private String id;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String address;
    private double totalAmount;
    private List<CartItem> cartItems;

    public DeliveryInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(DeliveryInfo.class)
    }

    public DeliveryInfo(String userName, String userEmail, String userPhone, String address, double totalAmount, List<CartItem> cartItems) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.address = address;
        this.totalAmount = totalAmount;
        this.cartItems = cartItems;
    }

    // Getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
