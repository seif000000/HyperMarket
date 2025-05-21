package com.hypermarket.Inventory;

public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String expiryDate;

    public Product(int id, String name, int quantity, double price, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Price: " + price + ", Expiry Date: " + expiryDate;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getExpiryDate() { return expiryDate; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}