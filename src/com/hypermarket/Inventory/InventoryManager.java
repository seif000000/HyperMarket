package com.hypermarket.Inventory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private final List<Product> products;
    private final List<Product> damagedProducts; // للمنتجات التالفة (خاص بالـ admin)
    private final List<Product> returnedProducts; // للمنتجات المرتجعة (يمكن للـ user إرجاع منتج)
    private final String filename = "products.txt";

    public InventoryManager() {
        products = new ArrayList<>();
        damagedProducts = new ArrayList<>();
        returnedProducts = new ArrayList<>();
        loadProducts();
    }

    private void loadProducts() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error creating the file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length != 5) {
                        System.out.println("Skipping malformed line: " + line);
                        continue;
                    }
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        int quantity = Integer.parseInt(parts[2].trim());
                        double price = Double.parseDouble(parts[3].trim());
                        String expiryDate = parts[4].trim();
                        if (name.isEmpty() || expiryDate.isEmpty()) {
                            System.out.println("Skipping invalid line (empty name or expiry date): " + line);
                            continue;
                        }
                        products.add(new Product(id, name, quantity, price, expiryDate));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing line: " + line + ". Skipping...");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private void saveProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getQuantity() + "," +
                        product.getPrice() + "," + product.getExpiryDate() + "\n");
            }
            System.out.println("Products saved successfully to " + new File(filename).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving the file: " + e.getMessage());
            System.out.println("Warning: Changes may not have been saved!");
        }
    }

    public void addProduct(int id, String name, int quantity, double price, String expiryDate) {
        if (id <= 0) {
            System.out.println("Error: ID must be positive!");
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty!");
            return;
        }
        if (quantity < 0) {
            System.out.println("Error: Quantity cannot be negative!");
            return;
        }
        if (price < 0) {
            System.out.println("Error: Price cannot be negative!");
            return;
        }
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            System.out.println("Error: Expiry date cannot be empty!");
            return;
        }

        for (Product product : products) {
            if (product.getId() == id) {
                System.out.println("Error: ID already exists!");
                return;
            }
        }
        products.add(new Product(id, name, quantity, price, expiryDate));
        saveProducts();
        System.out.println("Product added successfully!");
    }

    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in the inventory!");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    public Product searchProduct(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public void updateProduct(int id, String name, Integer quantity, Double price, String expiryDate) {
        Product product = searchProduct(id);
        if (product == null) {
            System.out.println("Cannot update, product not found!");
            return;
        }
        System.out.println("Product found:");
        System.out.println(product);
        if (name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }
        if (quantity != null) {
            if (quantity < 0) {
                System.out.println("Error: Quantity cannot be negative! Skipping...");
            } else {
                product.setQuantity(quantity);
            }
        }
        if (price != null) {
            if (price < 0) {
                System.out.println("Error: Price cannot be negative! Skipping...");
            } else {
                product.setPrice(price);
            }
        }
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            product.setExpiryDate(expiryDate);
        }
        saveProducts();
        System.out.println("Product updated successfully!");
    }

    public void deleteProduct(int id) {
        Product product = searchProduct(id);
        if (product == null) {
            System.out.println("Cannot delete, product not found!");
            return;
        }
        products.remove(product);
        saveProducts();
        System.out.println("Product deleted successfully!");
    }

    // وضع علامة على المنتج كتالف (خاص بالـ admin)
    public void markAsDamaged(int id) {
        Product product = searchProduct(id);
        if (product == null) {
            System.out.println("Cannot mark, product not found!");
            return;
        }
        products.remove(product);
        damagedProducts.add(product);
        saveProducts();
        System.out.println("Product marked as damaged successfully!");
    }

    // وضع علامة على المنتج كمرتجع (يمكن للـ user استخدامها)
    public void markAsReturned(int id) {
        Product product = searchProduct(id);
        if (product == null) {
            System.out.println("Cannot mark, product not found!");
            return;
        }
        products.remove(product);
        returnedProducts.add(product);
        saveProducts();
        System.out.println("Product marked as returned successfully!");
    }

    // عرض المنتجات التالفة (خاص بالـ admin)
    public void displayDamagedProducts() {
        if (damagedProducts.isEmpty()) {
            System.out.println("No damaged products.");
        } else {
            System.out.println("=== Damaged Products ===");
            for (Product product : damagedProducts) {
                System.out.println(product);
            }
        }
    }

    // عرض المنتجات المرتجعة (متاح للـ admin والـ user)
    public void displayReturnedProducts() {
        if (returnedProducts.isEmpty()) {
            System.out.println("No returned products.");
        } else {
            System.out.println("=== Returned Products ===");
            for (Product product : returnedProducts) {
                System.out.println(product);
            }
        }
    }

    public List<Product> getAllProducts() {
        return List.of();
    }

    public List<Product> getReturnedProducts() {
        return List.of();
    }

    public List<Product> getDamagedProducts() {
        return List.of();
    }

    public void findProductsByName(String name) {
    }
}