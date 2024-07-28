package com.group4.kidomtoystore.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderMethod implements Parcelable {
    int icon;
    String title;
    String description;
    double price;

    public OrderMethod(int icon, String title, String description, double price) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public static final Creator<OrderMethod> CREATOR = new Creator<OrderMethod>() {
        @Override
        public OrderMethod createFromParcel(Parcel in) {
            return new OrderMethod(in);
        }

        @Override
        public OrderMethod[] newArray(int size) {
            return new OrderMethod[size];
        }
    };

    public OrderMethod(Parcel in) {
        icon = in.readInt();
        title = in.readString();
        description = in.readString();
        price = in.readDouble();
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(price);
    }
}
