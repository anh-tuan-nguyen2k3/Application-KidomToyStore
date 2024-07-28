package com.group4.kidomtoystore.Models;

public class Review {
    private String content;
    private String imageUrl;
    private float rating;

    public Review(String content, String imageUrl, float rating) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
