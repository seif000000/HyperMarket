package com.hypermarket.offer;

public class Offer {
    private int offerId;
    private int productId;
    private double discount;
    private String startDate;
    private String endDate;

    public Offer(int offerId, int productId, double discount, String startDate, String endDate) {
        this.offerId = offerId;
        this.productId = productId;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public int getOfferId() { return offerId; }
    public int getProductId() { return productId; }
    public double getDiscount() { return discount; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }

    // Setters
    public void setOfferId(int offerId) { this.offerId = offerId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "Offer [ID=" + offerId + ", Product ID=" + productId + ", Discount=" + discount + "%, Start Date=" + startDate + ", End Date=" + endDate + "]";
    }

    public char[] getId() {
        return new char[0];
    }
}