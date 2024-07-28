package com.group4.kidomtoystore.Models;

public class OrderManagement {
    private String id;
    private String name;
    private int quantity;
    private double amount;
    private String status;
    private String thumb;
    private CartItem item;

    public OrderManagement(String id, String name, int quantity, double amount, String status, String thumb) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public CartItem getItem() {
        return item;
    }

    public void setItem(CartItem item) {
        this.item = item;
    }
}