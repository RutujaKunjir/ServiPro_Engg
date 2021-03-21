package com.example.servipro_engg.List;

public class ProductAddList
{
   private String productName;
    private String productPrice;
    private String productQty;
    private String totalAmount;

    public ProductAddList() {
    }

    public ProductAddList( String productName, String productPrice, String productQty, String totalAmount) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.totalAmount = totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
