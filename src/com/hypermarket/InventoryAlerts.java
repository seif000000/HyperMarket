package com.hypermarket;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Product {
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

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getExpiryDate() { return expiryDate; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}

public class InventoryAlerts {
    private List<Product> products;
    private String filename = "products.txt";
    private String damagedReturnsFile = "damaged_returns.txt";

    public InventoryAlerts() {
        products = new ArrayList<>();
        loadProducts();
    }

    private void loadProducts() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    String expiryDate = parts[4];
                    products.add(new Product(id, name, quantity, price, expiryDate));
                }
            }
        } catch (IOException e) {
        }
    }

    public void checkLowStock() {
        for (Product product : products) {
            if (product.getQuantity() < 10) {
                System.out.println("Low stock alert: " + product.getName() + " (Qty: " + product.getQuantity() + ")");
            }
        }
    }

    public void checkExpiryDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();

        for (Product product : products) {
            try {
                Date expiry = sdf.parse(product.getExpiryDate());
                long diff = expiry.getTime() - today.getTime();
                long daysLeft = diff / (1000 * 60 * 60 * 24);
                if (daysLeft <= 7) {
                    System.out.println("Expiry alert: " + product.getName() + " expires in " + daysLeft + " day(s)");
                }
            } catch (ParseException e) {
                System.out.println("Invalid expiry date for product: " + product.getName());
            }
        }
    }

    public void addDamagedOrReturnedProduct(int id, String reason) {
        for (Product product : products) {
            if (product.getId() == id) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(damagedReturnsFile, true))) {
                    writer.write(product.getId() + "," + product.getName() + "," + reason + "\n");
                    System.out.println("Product marked as " + reason);
                    return;
                } catch (IOException e) {
                    System.out.println("Error writing to file!");
                }
            }
        }
        System.out.println("Product not found!");
    }

    public void displayDamagedAndReturned() {
        File file = new File(damagedReturnsFile);
        if (!file.exists()) {
            System.out.println("No damaged or returned products recorded yet.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(damagedReturnsFile))) {
            String line;
            System.out.println("Damaged and Returned Products:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading damaged/returned file!");
        }
    }

    public void startNotifications() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkLowStock();
                checkExpiryDates();
            }
        }, 0, 60000);
    }

    public static void main(String[] args) {
        InventoryAlerts alerts = new InventoryAlerts();
        alerts.startNotifications();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Inventory Alerts Menu ===");
            System.out.println("1. Check Low Stock");
            System.out.println("2. Check Expiry Dates");
            System.out.println("3. Add Damaged/Returned Product");
            System.out.println("4. Display Damaged/Returned Products");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    alerts.checkLowStock();
                    break;
                case 2:
                    alerts.checkExpiryDates();
                    break;
                case 3:
                    System.out.print("Enter Product ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter reason (Damaged / Returned): ");
                    String reason = scanner.nextLine();
                    alerts.addDamagedOrReturnedProduct(id, reason);
                    break;
                case 4:
                    alerts.displayDamagedAndReturned();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}