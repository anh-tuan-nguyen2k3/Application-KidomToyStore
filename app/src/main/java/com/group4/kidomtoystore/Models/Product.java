package com.group4.kidomtoystore.Models;

public class Product {
    private String Brand;
    private int CategoryId;
    private String Description;
    private String id;
    private String Name;
    private String imgURL;
    private double Price;
    private double Star;
    private int Stock;
    private int Sale;
    private boolean recommend;

    public int getSale() {
        return Sale;
    }

    public void setSale(int sale) {
        Sale = sale;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product() {
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }




    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imagePath) {
        imgURL = imagePath;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    @Override
    public String toString() {
        return Name;
    }
}