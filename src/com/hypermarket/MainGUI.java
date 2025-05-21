package com.hypermarket;

import com.hypermarket.Inventory.InventoryManager;
import com.hypermarket.Inventory.Product;
import com.hypermarket.offer.Offer;
import com.hypermarket.offer.OfferManager;
import com.hypermarket.sales.Order;
import com.hypermarket.sales.SalesManager;
import com.hypermarket.user.User;
import com.hypermarket.user.UserManager;
import com.hypermarket.admin.AdminManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI {
    private JFrame frame;
    private UserManager userManager;
    private AdminManager adminManager;
    private InventoryManager inventoryManager;
    private OfferManager offerManager;
    private SalesManager salesManager;
    private User loggedInUser;

    public MainGUI() {
        userManager = new UserManager();
        adminManager = new AdminManager();
        inventoryManager = new InventoryManager();
        offerManager = new OfferManager();
        salesManager = new SalesManager(inventoryManager);
        initialize();
    }

    private void initialize() {
        frame = new JFrame("HyperMarket System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        showLoginPanel();
        frame.setVisible(true);
    }

    private void showLoginPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("HyperMarket System Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            loggedInUser = userManager.login(username, password);
            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + loggedInUser.getUsername());
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        JButton createUserButton = new JButton("Create New User");
        createUserButton.addActionListener(e -> showCreateUserPanel(false));
        panel.add(createUserButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showCreateUserPanel(boolean isAdmin) {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(isAdmin ? "Admin: Create New User" : "Create New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        panel.add(roleCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton createButton = new JButton("Create User");
        createButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            boolean created;
            if (isAdmin && loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin")) {
                created = adminManager.createUser(loggedInUser, username, password, role);
            } else {
                created = userManager.createUser(username, password, role);
            }
            if (created) {
                JOptionPane.showMessageDialog(frame, "User created successfully.");
                if (isAdmin) {
                    showMainMenu();
                } else {
                    showLoginPanel();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(createButton, gbc);

        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (isAdmin) {
                showMainMenu();
            } else {
                showLoginPanel();
            }
        });
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showMainMenu() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JButton updateDataButton = new JButton("Update My Data");
        updateDataButton.addActionListener(e -> showUpdateDataPanel());
        panel.add(updateDataButton, gbc);

        gbc.gridx = 1;
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            JOptionPane.showMessageDialog(frame, "Logged out successfully.");
            showLoginPanel();
        });
        panel.add(logoutButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton manageInventoryButton = new JButton("Manage Inventory");
        manageInventoryButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(manageInventoryButton, gbc);

        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            gbc.gridy = 3;
            JButton createUserButton = new JButton("Create User (Admin)");
            createUserButton.addActionListener(e -> showCreateUserPanel(true));
            panel.add(createUserButton, gbc);

            gbc.gridy = 4;
            JButton updateUserButton = new JButton("Update User by ID (Admin)");
            updateUserButton.addActionListener(e -> showUpdateUserByIdPanel());
            panel.add(updateUserButton, gbc);

            gbc.gridy = 5;
            JButton deleteUserButton = new JButton("Delete User by ID (Admin)");
            deleteUserButton.addActionListener(e -> showDeleteUserPanel());
            panel.add(deleteUserButton, gbc);

            gbc.gridy = 6;
            JButton viewUsersButton = new JButton("View All Users (Admin)");
            viewUsersButton.addActionListener(e -> viewAllUsers());
            panel.add(viewUsersButton, gbc);
        }

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showUpdateDataPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Update My Data");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("New Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String newUsername = usernameField.getText().isEmpty() ? null : usernameField.getText();
            String newPassword = new String(passwordField.getPassword()).isEmpty() ? null : new String(passwordField.getPassword());
            boolean updated = userManager.updateOwnData(loggedInUser, newUsername, newPassword);
            if (updated) {
                JOptionPane.showMessageDialog(frame, "Data updated successfully.");
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(updateButton, gbc);

        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showUpdateUserByIdPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Update User by ID");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("New Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(idField.getText());
                String newUsername = usernameField.getText().isEmpty() ? null : usernameField.getText();
                String newPassword = new String(passwordField.getPassword()).isEmpty() ? null : new String(passwordField.getPassword());
                boolean updated = adminManager.updateUser(loggedInUser, userId, newUsername, newPassword);
                if (updated) {
                    JOptionPane.showMessageDialog(frame, "User updated successfully.");
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(updateButton, gbc);

        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showDeleteUserPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Delete User by ID");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(idField.getText());
                boolean deleted = adminManager.deleteUser(loggedInUser, userId);
                if (deleted) {
                    JOptionPane.showMessageDialog(frame, "User deleted successfully.");
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(deleteButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void viewAllUsers() {
        List<User> users = adminManager.getAllUsers(loggedInUser);
        if (users == null || users.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No users found.", "Users List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("All Users:\n");
        sb.append(String.format("%-5s %-15s %-10s%n", "ID", "Username", "Role"));
        sb.append("----------------------------\n");

        for (User user : users) {
            sb.append(String.format("%-5d %-15s %-10s%n",
                    user.getId(), user.getUsername(), user.getRole()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(frame, scrollPane, "Users List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInventoryManagementPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Inventory Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JButton addProductButton = new JButton("Add New Product");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            addProductButton.addActionListener(e -> showAddProductPanel());
        } else {
            addProductButton.setEnabled(false);
        }
        panel.add(addProductButton, gbc);

        gbc.gridx = 1;
        JButton displayProductsButton = new JButton("Display All Products");
        displayProductsButton.addActionListener(e -> displayAllProducts());
        panel.add(displayProductsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton searchProductButton = new JButton("Search for a Product");
        searchProductButton.addActionListener(e -> showSearchProductPanel());
        panel.add(searchProductButton, gbc);

        gbc.gridx = 1;
        JButton updateProductButton = new JButton("Update a Product");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            updateProductButton.addActionListener(e -> showUpdateProductPanel());
        } else {
            updateProductButton.setEnabled(false);
        }
        panel.add(updateProductButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton deleteProductButton = new JButton("Delete a Product");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            deleteProductButton.addActionListener(e -> showDeleteProductPanel());
        } else {
            deleteProductButton.setEnabled(false);
        }
        panel.add(deleteProductButton, gbc);

        gbc.gridx = 1;
        JButton markDamagedButton = new JButton("Mark Product as Damaged");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            markDamagedButton.addActionListener(e -> showMarkDamagedPanel());
        } else {
            markDamagedButton.setEnabled(false);
        }
        panel.add(markDamagedButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton markReturnedButton = new JButton("Mark Product as Returned");
        markReturnedButton.addActionListener(e -> showMarkReturnedPanel());
        panel.add(markReturnedButton, gbc);

        gbc.gridx = 1;
        JButton viewDamagedButton = new JButton("View Damaged Products");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            viewDamagedButton.addActionListener(e -> displayDamagedProducts());
        } else {
            viewDamagedButton.setEnabled(false);
        }
        panel.add(viewDamagedButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton viewReturnedButton = new JButton("View Returned Products");
        viewReturnedButton.addActionListener(e -> displayReturnedProducts());
        panel.add(viewReturnedButton, gbc);

        gbc.gridx = 1;
        JButton addOfferButton = new JButton("Add New Offer");
        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            addOfferButton.addActionListener(e -> showAddOfferPanel());
        } else {
            addOfferButton.setEnabled(false);
        }
        panel.add(addOfferButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JButton listOffersButton = new JButton("List All Offers");
        listOffersButton.addActionListener(e -> listAllOffers());
        panel.add(listOffersButton, gbc);

        gbc.gridx = 1;
        JButton createOrderButton = new JButton("Create New Order");
        createOrderButton.addActionListener(e -> createNewOrder());
        panel.add(createOrderButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JButton addToOrderButton = new JButton("Add Product to Order");
        addToOrderButton.addActionListener(e -> showAddProductToOrderPanel());
        panel.add(addToOrderButton, gbc);

        gbc.gridx = 1;
        JButton completeOrderButton = new JButton("Complete Order");
        completeOrderButton.addActionListener(e -> showCompleteOrderPanel());
        panel.add(completeOrderButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JButton cancelOrderButton = new JButton("Cancel Order");
        cancelOrderButton.addActionListener(e -> showCancelOrderPanel());
        panel.add(cancelOrderButton, gbc);

        gbc.gridx = 1;
        JButton displayOrdersButton = new JButton("Display All Orders");
        displayOrdersButton.addActionListener(e -> displayAllOrders());
        panel.add(displayOrdersButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainMenu());
        panel.add(backButton, gbc);

        frame.add(new JScrollPane(panel));
        frame.revalidate();
        frame.repaint();
    }

    private void showAddProductPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        JTextField quantityField = new JTextField(15);
        panel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        JTextField priceField = new JTextField(15);
        panel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        JTextField expiryField = new JTextField(15);
        panel.add(expiryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                String expiryDate = expiryField.getText();
                inventoryManager.addProduct(id, name, quantity, price, expiryDate);
                JOptionPane.showMessageDialog(frame, "Product added successfully.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton, gbc);

        gbc.gridy = 7;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void displayAllProducts() {
        List<Product> products = inventoryManager.getAllProducts();
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No products found.", "Products List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("All Products:\n");
        sb.append(String.format("%-8s %-20s %-10s %-10s %-15s%n",
                "ID", "Name", "Quantity", "Price", "Expiry Date"));
        sb.append("------------------------------------------------------------\n");

        for (Product p : products) {
            sb.append(String.format("%-8d %-20s %-10d %-10.2f %-15s%n",
                    p.getId(), p.getName(), p.getQuantity(), p.getPrice(), p.getExpiryDate()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, "Products List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSearchProductPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Search Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Product product = inventoryManager.searchProduct(id);
                if (product != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Product Found:\n" +
                                    "ID: " + product.getId() + "\n" +
                                    "Name: " + product.getName() + "\n" +
                                    "Quantity: " + product.getQuantity() + "\n" +
                                    "Price: " + product.getPrice() + "\n" +
                                    "Expiry Date: " + product.getExpiryDate(),
                            "Product Details", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(searchButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showUpdateProductPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Update Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("New Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("New Quantity:"), gbc);

        gbc.gridx = 1;
        JTextField quantityField = new JTextField(15);
        panel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("New Price:"), gbc);

        gbc.gridx = 1;
        JTextField priceField = new JTextField(15);
        panel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("New Expiry Date:"), gbc);

        gbc.gridx = 1;
        JTextField expiryField = new JTextField(15);
        panel.add(expiryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText().isEmpty() ? null : nameField.getText();
                Integer quantity = quantityField.getText().isEmpty() ? null : Integer.parseInt(quantityField.getText());
                Double price = priceField.getText().isEmpty() ? null : Double.parseDouble(priceField.getText());
                String expiryDate = expiryField.getText().isEmpty() ? null : expiryField.getText();
                inventoryManager.updateProduct(id, name, quantity, price, expiryDate);
                JOptionPane.showMessageDialog(frame, "Product updated successfully.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(updateButton, gbc);

        gbc.gridy = 7;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showDeleteProductPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Delete Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                inventoryManager.deleteProduct(id);
                JOptionPane.showMessageDialog(frame, "Product deleted successfully.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(deleteButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showMarkDamagedPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Mark Product as Damaged");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton markButton = new JButton("Mark as Damaged");
        markButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                inventoryManager.markAsDamaged(id);
                JOptionPane.showMessageDialog(frame, "Product marked as damaged.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(markButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showMarkReturnedPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Mark Product as Returned");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton markButton = new JButton("Mark as Returned");
        markButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                inventoryManager.markAsReturned(id);
                JOptionPane.showMessageDialog(frame, "Product marked as returned.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(markButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void displayDamagedProducts() {
        List<Product> products = inventoryManager.getDamagedProducts();
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No damaged products found.", "Damaged Products", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Damaged Products:\n");
        sb.append(String.format("%-8s %-20s %-10s %-10s %-15s%n",
                "ID", "Name", "Quantity", "Price", "Expiry Date"));
        sb.append("------------------------------------------------------------\n");

        for (Product p : products) {
            sb.append(String.format("%-8d %-20s %-10d %-10.2f %-15s%n",
                    p.getId(), p.getName(), p.getQuantity(), p.getPrice(), p.getExpiryDate()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, "Damaged Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayReturnedProducts() {
        List<Product> products = inventoryManager.getReturnedProducts();
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No returned products found.", "Returned Products", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Returned Products:\n");
        sb.append(String.format("%-8s %-20s %-10s %-10s %-15s%n",
                "ID", "Name", "Quantity", "Price", "Expiry Date"));
        sb.append("------------------------------------------------------------\n");

        for (Product p : products) {
            sb.append(String.format("%-8d %-20s %-10d %-10.2f %-15s%n",
                    p.getId(), p.getName(), p.getQuantity(), p.getPrice(), p.getExpiryDate()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, "Returned Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddOfferPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Offer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Offer ID:"), gbc);

        gbc.gridx = 1;
        JTextField offerIdField = new JTextField(15);
        panel.add(offerIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField productIdField = new JTextField(15);
        panel.add(productIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Discount (%):"), gbc);

        gbc.gridx = 1;
        JTextField discountField = new JTextField(15);
        panel.add(discountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        JTextField startDateField = new JTextField(15);
        panel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        JTextField endDateField = new JTextField(15);
        panel.add(endDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Offer");
        addButton.addActionListener(e -> {
            try {
                int offerId = Integer.parseInt(offerIdField.getText());
                int productId = Integer.parseInt(productIdField.getText());
                double discount = Double.parseDouble(discountField.getText());
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                Offer offer = new Offer(offerId, productId, discount, startDate, endDate);
                offerManager.addOffer(offer);
                JOptionPane.showMessageDialog(frame, "Offer added successfully.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton, gbc);

        gbc.gridy = 7;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void listAllOffers() {
        List<Offer> offers = offerManager.getAllOffers();
        if (offers == null || offers.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No offers found.", "Offers List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("All Offers:\n");
        sb.append(String.format("%-8s %-10s %-10s %-15s %-15s%n",
                "Offer ID", "Product ID", "Discount", "Start Date", "End Date"));
        sb.append("------------------------------------------------------------\n");

        for (Offer o : offers) {
            sb.append(String.format("%-8d %-10d %-10.2f%% %-15s %-15s%n",
                    o.getOfferId(), o.getProductId(), o.getDiscount(), o.getStartDate(), o.getEndDate()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, "Offers List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createNewOrder() {
        Order newOrder = salesManager.createNewOrder();
        JOptionPane.showMessageDialog(frame, "New order created with ID: " + newOrder.getId());
    }

    private void showAddProductToOrderPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add Product to Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        JTextField orderIdField = new JTextField(15);
        panel.add(orderIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        JTextField productIdField = new JTextField(15);
        panel.add(productIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        JTextField quantityField = new JTextField(15);
        panel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(e -> {
            try {
                String orderId = orderIdField.getText();
                int productId = Integer.parseInt(productIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                salesManager.addProductToOrder(orderId, productId, quantity);
                JOptionPane.showMessageDialog(frame, "Product added to order.");
                showInventoryManagementPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton, gbc);

        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showCompleteOrderPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Complete Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        JTextField orderIdField = new JTextField(15);
        panel.add(orderIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton completeButton = new JButton("Complete Order");
        completeButton.addActionListener(e -> {
            String orderId = orderIdField.getText();
            salesManager.completeOrder(orderId);
            JOptionPane.showMessageDialog(frame, "Order completed.");
            showInventoryManagementPanel();
        });
        panel.add(completeButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showCancelOrderPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Cancel Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        JTextField orderIdField = new JTextField(15);
        panel.add(orderIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton cancelButton = new JButton("Cancel Order");
        cancelButton.addActionListener(e -> {
            String orderId = orderIdField.getText();
            salesManager.cancelOrder(orderId);
            JOptionPane.showMessageDialog(frame, "Order cancelled.");
            showInventoryManagementPanel();
        });
        panel.add(cancelButton, gbc);

        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showInventoryManagementPanel());
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void displayAllOrders() {
        List<Order> orders = salesManager.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No orders found.", "Orders List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("All Orders:\n");
        for (Order o : orders) {
            sb.append("------------------------------------------------------------\n");
            sb.append(String.format("Order ID: %s%nStatus: %s%n", o.getId(), o.getStatus()));
            sb.append("Products:\n");

            for (Product p : o.getProducts()) {
                sb.append(String.format("  - %s (ID: %d, Qty: %d, Price: %.2f)%n",
                        p.getName(), p.getId(), p.getQuantity(), p.getPrice()));
            }

            sb.append(String.format("Total: %.2f%n%n", o.getTotal()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, "Orders List", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().frame.setVisible(true));
    }
}