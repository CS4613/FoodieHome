package com.dileep.foodiehome.pojos;

import java.util.ArrayList;

public class BestsellersPojo {
    String productName,productId,productCost,productCategory,whatsAppNumber;
    ArrayList<String> Colors;
    ArrayList<String>imageUrls;

    public BestsellersPojo() {
    }

    public BestsellersPojo(String productName, String productId, String productCost, String productCategory, String whatsAppNumber, ArrayList<String> colors, ArrayList<String> imageUrls) {
        this.productName = productName;
        this.productId = productId;
        this.productCost = productCost;
        this.productCategory = productCategory;
        this.whatsAppNumber = whatsAppNumber;
        Colors = colors;
        this.imageUrls = imageUrls;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductCost() {
        return productCost;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getWhatsAppNumber() {
        return whatsAppNumber;
    }

    public ArrayList<String> getColors() {
        return Colors;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setWhatsAppNumber(String whatsAppNumber) {
        this.whatsAppNumber = whatsAppNumber;
    }

    public void setColors(ArrayList<String> colors) {
        Colors = colors;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
