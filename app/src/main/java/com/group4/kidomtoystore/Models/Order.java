package com.group4.kidomtoystore.Models;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    ArrayList<CartItem> items;
    String address;
    String payment;
    String delivery;
    double deliveryFee;
    String discount;
    double discountAmount;
    double amount;
    String status;
    String time;

    public Order(ArrayList<CartItem> items, String address, String payment, String delivery, double deliveryFee, String discount, double discountAmount, double amount, String status, String time) {
        this.items = items;
        this.address = address;
        this.payment = payment;
        this.delivery = delivery;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.amount = amount;
        this.status = status;
        this.time = time;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
