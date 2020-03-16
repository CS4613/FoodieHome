package com.dileep.foodiehome.pojos;

import java.util.ArrayList;

public class UploadProductPojo {
    String productName,productId,productCost,productCategory,whatsAppNumber,admin;
    ArrayList<String>colors;
    ArrayList<String>imageUrls;
    ArrayList<String>imageNames;



    public UploadProductPojo() {

    }

    public UploadProductPojo(String productName, String productId, String productCost, String productCategory, String whatsAppNumber, ArrayList<String> colors, ArrayList<String> imageUrls,String admin,ArrayList<String> imageNames) {
        this.productName = productName;
        this.productId = productId;
        this.productCost = productCost;
        this.productCategory = productCategory;
        this.whatsAppNumber = whatsAppNumber;
        this.colors = colors;
        this.imageUrls = imageUrls;
        this.admin=admin;
        this.imageNames=imageNames;
    }

    public String getAdmin() {
        return admin;
    }

    public ArrayList<String> getImageNames() {
        return imageNames;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setImageNames(ArrayList<String> imageNames) {
        this.imageNames = imageNames;
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
        return colors;
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
        colors = colors;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
