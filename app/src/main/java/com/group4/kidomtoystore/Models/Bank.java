package com.group4.kidomtoystore.Models;

public class Bank {
    int bankThumb;
    String bankName;

    public Bank(int bankThumb, String bankName) {
        this.bankThumb = bankThumb;
        this.bankName = bankName;
    }

    public int getBankThumb() {
        return bankThumb;
    }

    public void setBankThumb(int bankThumb) {
        this.bankThumb = bankThumb;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankThumb=" + bankThumb +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
