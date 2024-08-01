package com.example.hives;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String imageUrl;

    // Constructor, getters, and setters

    public CartItem(String name, String category, double price, int quantity, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    protected CartItem(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getTotalPrice() {
        return price * quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeDouble(price);
        parcel.writeInt(quantity);
        parcel.writeString(imageUrl);
    }
}
