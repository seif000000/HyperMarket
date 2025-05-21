package com.hypermarket.sales;

import com.hypermarket.Inventory.Product;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String id;
    private final List<Product> products;
    private String status; // "OPEN", "COMPLETED", "CANCELLED"
    private double totalPrice;

    public Order(String id) {
        this.id = id;
        this.products = new ArrayList<>();
        this.status = "OPEN";
        this.totalPrice = 0.0;
    }

    public void addProduct(Product product, int quantity) {
        if (product != null && quantity > 0 && product.getQuantity() >= quantity) {
            Product orderedProduct = new Product(
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    product.getExpiryDate()
            );
            products.add(orderedProduct);
            totalPrice += product.getPrice() * quantity;
        }
    }

    public void removeProduct(int productId) {
        products.removeIf(p -> p.getId() == productId);
        calculateTotal();
    }

    private void calculateTotal() {
        totalPrice = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    public void setStatus(String status) {
        if (List.of("OPEN", "COMPLETED", "CANCELLED").contains(status)) {
            this.status = status;
        }
    }

    // Getters
    public String getId() { return id; }
    public List<Product> getProducts() { return products; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id)
                .append("\nStatus: ").append(status)
                .append("\nTotal: ").append(totalPrice)
                .append("\nProducts:\n");

        products.forEach(p -> sb.append("- ").append(p).append("\n"));

        return sb.toString();
    }

    public char[] getTotal() {
        return new char[0];
    }
}