package com.group4.kidomtoystore.Models;

public class PaymentCard  {
    private String bankName;
    private String userName;
    private String bankNumber;
    private String cvv;
    private String expiryDate;


    public PaymentCard( String userName, String bankName, String bankNumber, String expiryDate, String cvv) {
        this.bankName = bankName;
        this.userName = userName;
        this.bankNumber = bankNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    public PaymentCard() {
        // Constructor không đối số
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
