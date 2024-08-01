package com.example.hives;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private String category;
    private double price;
    private String imageUrl;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String name, String category, double price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    protected Product(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        imageUrl = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
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
        parcel.writeString(imageUrl);
    }
}
