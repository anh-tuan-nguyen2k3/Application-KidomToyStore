package com.group4.kidomtoystore.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Address implements Parcelable {
    String fullName;
    String phone;
    String addressDetail;
    String addressType;
    String id;

    public Address() {
    }

    public Address(String fullName, String phone, String addressDetail, String addressType) {
        this.fullName = fullName;
        this.phone = phone;
        this.addressDetail = addressDetail;
        this.addressType = addressType;
    }

    protected Address(Parcel in) {
        fullName = in.readString();
        phone = in.readString();
        addressDetail = in.readString();
        addressType = in.readString();
        id = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(phone);
        dest.writeString(addressDetail);
        dest.writeString(addressType);
        dest.writeString(id);
    }
}
