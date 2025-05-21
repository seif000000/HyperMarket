package com.hypermarket.sales;

import com.hypermarket.Inventory.InventoryManager;
import com.hypermarket.Inventory.Product;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesGUI {
    private final SalesManager salesManager;
    private JFrame mainFrame;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private Order currentOrder;
    private Color primaryColor = new Color(0, 120, 215);
    private Color secondaryColor = new Color(240, 240, 240);
    private Color accentColor = new Color(255, 140, 0);

    public SalesGUI(SalesManager salesManager) {
        this.salesManager = salesManager;
        initializeMainFrame();
        setupModernLookAndFeel();
    }

    private void setupModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.background", primaryColor);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("Table.selectionBackground", primaryColor.brighter());
            UIManager.put("Table.selectionForeground", Color.WHITE);
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
    }

    private void initializeMainFrame() {
        mainFrame = new JFrame("Hypermarket Sales Management System");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);

        // Create main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(secondaryColor);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create orders table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add menu bar
        mainFrame.setJMenuBar(createMenuBar());

        mainFrame.add(mainPanel);
        refreshOrdersTable();
        mainFrame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("SALES MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Order Processing Center");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 230, 230));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Add date/time label
        JLabel dateLabel = new JLabel(getCurrentDateTime());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(Color.WHITE);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0),
                BorderFactory.createLineBorder(new Color(220, 220, 220))
        ));
        tablePanel.setBackground(Color.WHITE);

        // Create orders table with modern styling
        String[] columnNames = {"Order ID", "Status", "Items", "Total", "Date"};
        ordersTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        ordersTable = new JTable(ordersTableModel);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ordersTable.setRowHeight(30);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setShowGrid(false);
        ordersTable.setIntercellSpacing(new Dimension(0, 0));
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ordersTable.getTableHeader().setBackground(primaryColor);
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        ordersTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(secondaryColor);

        // Create styled buttons
        JButton newOrderButton = createStyledButton("New Order", primaryColor);
        newOrderButton.addActionListener(e -> showNewOrderDialog());

        JButton viewDetailsButton = createStyledButton("View Details", new Color(70, 130, 180));
        viewDetailsButton.addActionListener(e -> showOrderDetails());

        JButton completeOrderButton = createStyledButton("Complete Order", new Color(34, 139, 34));
        completeOrderButton.addActionListener(e -> completeSelectedOrder());

        JButton cancelOrderButton = createStyledButton("Cancel Order", new Color(178, 34, 34));
        cancelOrderButton.addActionListener(e -> cancelSelectedOrder());

        JButton refreshButton = createStyledButton("Refresh", new Color(138, 43, 226));
        refreshButton.addActionListener(e -> refreshOrdersTable());

        buttonPanel.add(newOrderButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(completeOrderButton);
        buttonPanel.add(cancelOrderButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        styleMenu(fileMenu);

        JMenuItem exitItem = new JMenuItem("Exit");
        styleMenuItem(exitItem);
        exitItem.addActionListener(e -> mainFrame.dispose());

        fileMenu.add(exitItem);

        // Orders Menu
        JMenu ordersMenu = new JMenu("Orders");
        styleMenu(ordersMenu);

        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        styleMenuItem(refreshItem);
        refreshItem.addActionListener(e -> refreshOrdersTable());

        ordersMenu.add(refreshItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        styleMenu(helpMenu);

        JMenuItem aboutItem = new JMenuItem("About");
        styleMenuItem(aboutItem);
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(ordersMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void styleMenu(JMenu menu) {
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menu.setForeground(Color.DARK_GRAY);
    }

    private void styleMenuItem(JMenuItem menuItem) {
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setForeground(Color.DARK_GRAY);
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showNewOrderDialog() {
        JDialog dialog = new JDialog(mainFrame, "Create New Order", true);
        dialog.setSize(700, 550);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(mainFrame);
        dialog.getContentPane().setBackground(secondaryColor);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product to Order"));
        inputPanel.setBackground(Color.WHITE);

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField productIdField = new JTextField();
        productIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField quantityField = new JTextField();
        quantityField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        inputPanel.add(productIdLabel);
        inputPanel.add(productIdField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel()); // Empty cell
        inputPanel.add(new JLabel()); // Empty cell

        // Create products table
        String[] columns = {"Product ID", "Name", "Qty", "Price", "Subtotal"};
        DefaultTableModel productsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable productsTable = new JTable(productsModel);
        productsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productsTable.setRowHeight(25);
        productsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane productsScroll = new JScrollPane(productsTable);
        productsScroll.setBorder(BorderFactory.createTitledBorder("Order Items"));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add Product", primaryColor);
        addButton.addActionListener(e -> {
            try {
                int productId = Integer.parseInt(productIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity <= 0) {
                    showErrorDialog(dialog, "Quantity must be greater than zero");
                    return;
                }

                Product product = salesManager.getInventoryManager().searchProduct(productId);
                if (product == null) {
                    showErrorDialog(dialog, "Product not found");
                    return;
                }

                if (product.getQuantity() < quantity) {
                    showErrorDialog(dialog, "Insufficient stock available");
                    return;
                }

                if (currentOrder == null) {
                    currentOrder = salesManager.createNewOrder();
                }

                salesManager.addProductToOrder(currentOrder.getId(), productId, quantity);

                productsModel.addRow(new Object[]{
                        product.getId(),
                        product.getName(),
                        quantity,
                        String.format("$%.2f", product.getPrice()),
                        String.format("$%.2f", product.getPrice() * quantity)
                });

                productIdField.setText("");
                quantityField.setText("");

            } catch (NumberFormatException ex) {
                showErrorDialog(dialog, "Please enter valid numbers");
            }
        });

        JButton finishButton = createStyledButton("Complete Order", new Color(34, 139, 34));
        finishButton.addActionListener(e -> {
            if (currentOrder != null && !currentOrder.getProducts().isEmpty()) {
                salesManager.completeOrder(currentOrder.getId());
                refreshOrdersTable();
                currentOrder = null;
                dialog.dispose();
                showSuccessDialog("Order completed successfully");
            } else {
                showWarningDialog(dialog, "No products in the order");
            }
        });

        JButton cancelButton = createStyledButton("Cancel", new Color(178, 34, 34));
        cancelButton.addActionListener(e -> {
            if (currentOrder != null) {
                int option = JOptionPane.showConfirmDialog(dialog,
                        "Cancel this order?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    salesManager.cancelOrder(currentOrder.getId());
                    currentOrder = null;
                    dialog.dispose();
                }
            } else {
                dialog.dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(finishButton);
        buttonPanel.add(cancelButton);

        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(productsScroll, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showOrderDetails() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog(mainFrame, "Please select an order");
            return;
        }

        String orderId = (String) ordersTableModel.getValueAt(selectedRow, 0);
        Order order = salesManager.findOrder(orderId);

        if (order == null) {
            showErrorDialog(mainFrame, "Order not found");
            return;
        }

        JDialog detailsDialog = new JDialog(mainFrame, "Order Details: " + orderId, true);
        detailsDialog.setSize(650, 450);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setLocationRelativeTo(mainFrame);
        detailsDialog.getContentPane().setBackground(secondaryColor);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(createBoldLabel("Order ID:"));
        infoPanel.add(new JLabel(order.getId()));

        infoPanel.add(createBoldLabel("Status:"));
        infoPanel.add(new JLabel(getFormattedStatus(order.getStatus())));

        infoPanel.add(createBoldLabel("Date:"));
        infoPanel.add(new JLabel(getCurrentDateTime()));

        infoPanel.add(createBoldLabel("Total Items:"));
        infoPanel.add(new JLabel(String.valueOf(order.getProducts().size())));

        infoPanel.add(createBoldLabel("Total Amount:"));
        infoPanel.add(new JLabel(String.format("$%.2f", order.getTotalPrice())));

        // Create products table
        String[] columns = {"Product ID", "Name", "Qty", "Price", "Subtotal"};
        DefaultTableModel detailsModel = new DefaultTableModel(columns, 0);

        for (Product product : order.getProducts()) {
            detailsModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    String.format("$%.2f", product.getPrice()),
                    String.format("$%.2f", product.getPrice() * product.getQuantity())
            });
        }

        JTable detailsTable = new JTable(detailsModel);
        detailsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsTable.setRowHeight(25);
        detailsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Order Items"));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton printButton = createStyledButton("Print Receipt", primaryColor);
        printButton.addActionListener(e -> {
            try {
                detailsTable.print();
            } catch (Exception ex) {
                showErrorDialog(detailsDialog, "Error printing receipt");
            }
        });

        JButton closeButton = createStyledButton("Close", new Color(100, 100, 100));
        closeButton.addActionListener(e -> detailsDialog.dispose());

        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.add(contentPanel);
        detailsDialog.setVisible(true);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private void completeSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog(mainFrame, "Please select an order");
            return;
        }

        String orderId = (String) ordersTableModel.getValueAt(selectedRow, 0);
        String status = (String) ordersTableModel.getValueAt(selectedRow, 1);

        if ("Completed".equals(status)) {
            showWarningDialog(mainFrame, "Order is already completed");
            return;
        }

        int option = JOptionPane.showConfirmDialog(mainFrame,
                "Complete this order?",
                "Confirm Completion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            salesManager.completeOrder(orderId);
            refreshOrdersTable();
            showSuccessDialog("Order completed successfully");
        }
    }

    private void cancelSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog(mainFrame, "Please select an order");
            return;
        }

        String orderId = (String) ordersTableModel.getValueAt(selectedRow, 0);
        String status = (String) ordersTableModel.getValueAt(selectedRow, 1);

        if ("Cancelled".equals(status)) {
            showWarningDialog(mainFrame, "Order is already cancelled");
            return;
        }

        int option = JOptionPane.showConfirmDialog(mainFrame,
                "Cancel this order?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            salesManager.cancelOrder(orderId);
            refreshOrdersTable();
            showSuccessDialog("Order cancelled successfully");
        }
    }

    private void refreshOrdersTable() {
        ordersTableModel.setRowCount(0);
        List<Order> allOrders = salesManager.getAllOrders();

        for (Order order : allOrders) {
            ordersTableModel.addRow(new Object[]{
                    order.getId(),
                    getFormattedStatus(order.getStatus()),
                    order.getProducts().size(),
                    String.format("$%.2f", order.getTotalPrice()),
                    getCurrentDateTime()
            });
        }
    }

    private String getFormattedStatus(String status) {
        switch (status) {
            case "COMPLETED": return "Completed";
            case "CANCELLED": return "Cancelled";
            default: return "Open";
        }
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(mainFrame,
                "Hypermarket Sales Management System\nVersion 1.0\n\n© 2023 Hypermarket Inc.",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(mainFrame,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                InventoryManager inventoryManager = new InventoryManager();
                SalesManager salesManager = new SalesManager(inventoryManager);
                new SalesGUI(salesManager);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Failed to initialize application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}