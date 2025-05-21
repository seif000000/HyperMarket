package com.hypermarket.sales;

import com.hypermarket.Inventory.InventoryManager;
import com.hypermarket.Inventory.Product;
import com.hypermarket.util.FileUtil;
import com.hypermarket.util.IdGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private final List<Order> orders;
    private final InventoryManager inventoryManager;
    private final String ordersFile = "orders.txt";

    public SalesManager(InventoryManager inventoryManager) {
        this.orders = new ArrayList<>();
        this.inventoryManager = inventoryManager;
        loadOrders();
    }

    private void loadOrders() {
        List<String> lines = FileUtil.readLines(ordersFile);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Order order = new Order(parts[0]);
                    order.setStatus(parts[1]);

                    if (parts.length > 2) {
                        String[] products = parts[2].split("\\|");
                        for (String productStr : products) {
                            String[] productParts = productStr.split(",");
                            if (productParts.length == 5) {
                                int id = Integer.parseInt(productParts[0]);
                                String name = productParts[1];
                                int quantity = Integer.parseInt(productParts[2]);
                                double price = Double.parseDouble(productParts[3]);
                                String expiry = productParts[4];

                                Product product = new Product(id, name, quantity, price, expiry);
                                order.addProduct(product, quantity);
                            }
                        }
                    }
                    orders.add(order);
                }
            }
        }
    }

    private void saveOrders() {
        List<String> lines = new ArrayList<>();
        for (Order order : orders) {
            StringBuilder sb = new StringBuilder();
            sb.append(order.getId()).append(";")
                    .append(order.getStatus()).append(";");

            if (!order.getProducts().isEmpty()) {
                for (Product product : order.getProducts()) {
                    sb.append(product.getId()).append(",")
                            .append(product.getName()).append(",")
                            .append(product.getQuantity()).append(",")
                            .append(product.getPrice()).append(",")
                            .append(product.getExpiryDate()).append("|");
                }
                sb.deleteCharAt(sb.length() - 1); // Remove last "|"
            }

            lines.add(sb.toString());
        }

        FileUtil.writeLines(ordersFile, lines);
    }

    public Order createNewOrder() {
        String orderId = "ORD-" + IdGenerator.generateId();
        Order newOrder = new Order(orderId);
        orders.add(newOrder);
        saveOrders();
        return newOrder;
    }

    public void addProductToOrder(String orderId, int productId, int quantity) {
        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        Product product = inventoryManager.searchProduct(productId);
        if (product == null) {
            System.out.println("Product not found in inventory!");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("Not enough quantity in stock!");
            return;
        }

        order.addProduct(product, quantity);
        saveOrders();
        System.out.println("Product added to order successfully!");
    }

    public void cancelOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        order.setStatus("CANCELLED");
        saveOrders();
        System.out.println("Order cancelled successfully!");
    }

    public void completeOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        // Update inventory quantities
        for (Product orderedProduct : order.getProducts()) {
            Product inventoryProduct = inventoryManager.searchProduct(orderedProduct.getId());
            if (inventoryProduct != null) {
                int newQuantity = inventoryProduct.getQuantity() - orderedProduct.getQuantity();
                inventoryManager.updateProduct(
                        inventoryProduct.getId(),
                        null,
                        newQuantity,
                        null,
                        null
                );
            }
        }

        order.setStatus("COMPLETED");
        saveOrders();
        System.out.println("Order completed successfully!");
    }

    public void displayAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found!");
        } else {
            System.out.println("=== All Orders ===");
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("------------------");
            }
        }
    }

    Order findOrder(String orderId) {
        return orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public InventoryManager getInventoryManager() {
        return null;
    }

    public List<Order> getAllOrders() {
        return List.of();
    }
}