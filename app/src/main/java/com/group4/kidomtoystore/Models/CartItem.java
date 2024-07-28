package com.group4.kidomtoystore.Models;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class CartItem implements Parcelable {
    private String fireId;
    private String id;
    private String productName;
    private double productPrice;
    private String productThumb;
    private int productQuantity;

    public CartItem() {
        // Default constructor required for Parcelable
    }

    public CartItem(String fireId, String id, String productName, double productPrice, String productThumb, int productQuantity) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productThumb = productThumb;
        this.productQuantity = productQuantity;
        this.fireId = fireId;
    }

    public CartItem(Parcel in) {
        id = in.readString();
        productName = in.readString();
        productPrice = in.readDouble();
        productThumb = in.readString();
        productQuantity = in.readInt();
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
    public String getFireId() {return fireId;}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productName);
        dest.writeDouble(productPrice);
        dest.writeString(productThumb);
        dest.writeInt(productQuantity);
    }
    public String getId() {
        return id;
    }

    // Setter method for the unique ID (if needed)
    public void setId(String id) {
        this.id = id;
    }
}