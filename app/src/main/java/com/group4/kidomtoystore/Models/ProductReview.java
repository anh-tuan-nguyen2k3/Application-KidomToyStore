package com.group4.kidomtoystore.Models;

public class ProductReview {
    private int rating;
    private String content;
    private String imgURL;
    private String productID;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ProductReview() {
        // Không cần thực hiện bất kỳ công việc cụ thể nào ở đây
        // Constructor này chỉ cần tồn tại để Firebase có thể tạo đối tượng từ dữ liệu được đọc
    }

    public ProductReview(int rating, String content, String imgURL, String productID, String user) {
        this.rating = rating;
        this.content = content;
        this.imgURL = imgURL;
        this.productID = productID;
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
