package com.hypermarket;//package com.hypermarket;
import com.hypermarket.offer.Offer;
import com.hypermarket.user.User;
import com.hypermarket.user.UserManager;
import com.hypermarket.admin.AdminManager;
import com.hypermarket.Inventory.InventoryManager;
import com.hypermarket.Inventory.Product;
import com.hypermarket.offer.OfferManager;
import com.hypermarket.sales.SalesManager;
import com.hypermarket.sales.Order;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();
        AdminManager adminManager = new AdminManager();
        InventoryManager inventoryManager = new InventoryManager();
        OfferManager offerManager = new OfferManager();
        SalesManager salesManager = new SalesManager(inventoryManager);
        boolean running = true;
        User loggedInUser = null;

        while (running) {
            System.out.println("\n=== HyperMarket System ===");
            if (loggedInUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Create New User");
            } else {
                System.out.println("1. Update My Data");
                System.out.println("2. Logout");
                System.out.println("3. Manage Inventory");
                if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                    System.out.println("4. Create User (Admin)");
                    System.out.println("5. Update User by ID (Admin)");
                    System.out.println("6. Delete User by ID (Admin)");
                    System.out.println("7. View All Users (Admin)");
                }
            }
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (loggedInUser == null) {
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        loggedInUser = userManager.login(loginUsername, loginPassword);
                        if (loggedInUser != null) {
                            System.out.println("Login successful! Welcome, " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } else {
                        System.out.print("Enter new username (leave empty to keep current): ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter new password (leave empty to keep current): ");
                        String newPassword = scanner.nextLine();
                        boolean updated = userManager.updateOwnData(loggedInUser, newUsername, newPassword);
                        if (updated) {
                            System.out.println("Your data has been updated successfully.");
                        } else {
                            System.out.println("Failed to update data.");
                        }
                    }
                    break;
                case 2:
                    if (loggedInUser == null) {
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        System.out.print("Enter role (admin/user): ");
                        String role = scanner.nextLine();
                        boolean created = userManager.createUser(newUsername, newPassword, role);
                        if (created) {
                            System.out.println("User created successfully.");
                        } else {
                            System.out.println("Username already exists. Try a different username.");
                        }
                    } else {
                        loggedInUser = null;
                        System.out.println("You have logged out successfully.");
                    }
                    break;
                case 3:
                    if (loggedInUser != null) {
                        manageInventory(scanner, inventoryManager, offerManager, salesManager, loggedInUser);
                    }
                    break;
                case 4:
                    if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        System.out.print("Enter role (admin/user): ");
                        String role = scanner.nextLine();
                        boolean created = adminManager.createUser(loggedInUser, newUsername, newPassword, role);
                        if (created) {
                            System.out.println("User created successfully by admin.");
                        }
                    }
                    break;
                case 5:
                    if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter user ID to update: ");
                        int userId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new username (leave empty to keep current): ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Enter new password (leave empty to keep current): ");
                        String newPassword = scanner.nextLine();
                        boolean updated = adminManager.updateUser(loggedInUser, userId, newUsername, newPassword);
                        if (updated) {
                            System.out.println("User updated successfully by admin.");
                        } else {
                            System.out.println("Failed to update user.");
                        }
                    }
                    break;
                case 6:
                    if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter user ID to delete: ");
                        int userId = scanner.nextInt();
                        scanner.nextLine();
                        boolean deleted = adminManager.deleteUser(loggedInUser, userId);
                        if (deleted) {
                            System.out.println("User deleted successfully by admin.");
                        }
                    }
                    break;
                case 7:
                    if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        adminManager.viewAllUsers(loggedInUser);
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
        scanner.close();
    }

    private static void manageInventory(Scanner scanner, InventoryManager inventoryManager, OfferManager offerManager, SalesManager salesManager, User loggedInUser) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Inventory Management System ===");
            System.out.println("1. Add new product");
            System.out.println("2. Display all products");
            System.out.println("3. Search for a product");
            System.out.println("4. Update a product");
            System.out.println("5. Delete a product");
            System.out.println("6. Mark product as damaged");
            System.out.println("7. Mark product as returned");
            System.out.println("8. View damaged products");
            System.out.println("9. View returned products");
            System.out.println("10. Add new offer");
            System.out.println("11. List all offers");
            System.out.println("12. Create new order");
            System.out.println("13. Add product to order");
            System.out.println("14. Complete order");
            System.out.println("15. Cancel order");
            System.out.println("16. Display all orders");
            System.out.println("17. Back to main menu");

            System.out.print("Enter your choice (1-17): ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number between 1 and 17.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter the ID: ");
                        int id;
                        try {
                            id = scanner.nextInt();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid ID! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.print("Enter product name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity;
                        try {
                            quantity = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid quantity! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.print("Enter price: ");
                        double price;
                        try {
                            price = scanner.nextDouble();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid price! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.print("Enter expiry date (e.g., 2025-12-31): ");
                        String expiryDate = scanner.nextLine();
                        inventoryManager.addProduct(id, name, quantity, price, expiryDate);
                    } else {
                        System.out.println("Only admins can add products!");
                    }
                    break;
                case 2:
                    inventoryManager.displayProducts(); // متاح للكل
                    break;
                case 3:
                    System.out.print("Enter the ID to search: ");
                    int id;
                    try {
                        id = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid ID! Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    Product product = inventoryManager.searchProduct(id);
                    if (product != null) {
                        System.out.println("Product found:");
                        System.out.println(product);
                    } else {
                        System.out.println("Product not found!");
                    }
                    break;
                case 4:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter the ID to update: ");
                        int updateId;
                        try {
                            updateId = scanner.nextInt();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid ID! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.print("Enter new name (or press Enter to skip): ");
                        String name = scanner.nextLine().trim();
                        Integer quantity = null;
                        Double price = null;
                        String expiryDate = null;
                        System.out.print("Enter new quantity (or press Enter to skip): ");
                        String quantityInput = scanner.nextLine().trim();
                        if (!quantityInput.isEmpty()) {
                            try {
                                quantity = Integer.parseInt(quantityInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid quantity! Skipping...");
                            }
                        }
                        System.out.print("Enter new price (or press Enter to skip): ");
                        String priceInput = scanner.nextLine().trim();
                        if (!priceInput.isEmpty()) {
                            try {
                                price = Double.parseDouble(priceInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid price! Skipping...");
                            }
                        }
                        System.out.print("Enter new expiry date (or press Enter to skip): ");
                        String expiryDateInput = scanner.nextLine().trim();
                        if (!expiryDateInput.isEmpty()) {
                            expiryDate = expiryDateInput;
                        }
                        inventoryManager.updateProduct(updateId, name.isEmpty() ? null : name, quantity, price, expiryDate);
                    } else {
                        System.out.println("Only admins can update products!");
                    }
                    break;
                case 5:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter the ID to delete: ");
                        int deleteId;
                        try {
                            deleteId = scanner.nextInt();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid ID! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        inventoryManager.deleteProduct(deleteId);
                    } else {
                        System.out.println("Only admins can delete products!");
                    }
                    break;
                case 6:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter the ID of the product to mark as damaged: ");
                        int markId;
                        try {
                            markId = scanner.nextInt();
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid ID! Please enter a valid number.");
                            scanner.nextLine();
                            continue;
                        }
                        inventoryManager.markAsDamaged(markId);
                    } else {
                        System.out.println("Only admins can mark products as damaged!");
                    }
                    break;
                case 7:
                    System.out.print("Enter the ID of the product to mark as returned: ");
                    int returnId;
                    try {
                        returnId = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid ID! Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    inventoryManager.markAsReturned(returnId);
                    break;
                case 8:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        inventoryManager.displayDamagedProducts();
                    } else {
                        System.out.println("Only admins can view damaged products!");
                    }
                    break;
                case 9:
                    inventoryManager.displayReturnedProducts(); // متاح للكل
                    break;
                case 10:
                    if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                        System.out.print("Enter Offer ID: ");
                        int offerId = scanner.nextInt();
                        System.out.print("Enter Product ID: ");
                        int productId = scanner.nextInt();
                        System.out.print("Enter Discount (%): ");
                        double discount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Enter Start Date (YYYY-MM-DD): ");
                        String startDate = scanner.nextLine();
                        System.out.print("Enter End Date (YYYY-MM-DD): ");
                        String endDate = scanner.nextLine();
                        Offer offer = new Offer(offerId, productId, discount, startDate, endDate);
                        offerManager.addOffer(offer);
                    } else {
                        System.out.println("Only admins can add offers!");
                    }
                    break;
                case 11:
                    offerManager.listOffers(); // متاح للكل
                    break;
                case 12:
                    Order newOrder = salesManager.createNewOrder();
                    System.out.println("New order created with ID: " + newOrder.getId());
                    break;
                case 13:
                    System.out.print("Enter Order ID: ");
                    String orderId = scanner.nextLine();
                    System.out.print("Enter Product ID: ");
                    int productId;
                    try {
                        productId = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid Product ID! Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    System.out.print("Enter Quantity: ");
                    int quantity;
                    try {
                        quantity = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid Quantity! Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    salesManager.addProductToOrder(orderId, productId, quantity);
                    break;
                case 14:
                    System.out.print("Enter Order ID to complete: ");
                    String completeOrderId = scanner.nextLine();
                    salesManager.completeOrder(completeOrderId);
                    break;
                case 15:
                    System.out.print("Enter Order ID to cancel: ");
                    String cancelOrderId = scanner.nextLine();
                    salesManager.cancelOrder(cancelOrderId);
                    break;
                case 16:
                    salesManager.displayAllOrders(); // متاح للكل
                    break;
                case 17:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again!");
            }
        }
    }
}
