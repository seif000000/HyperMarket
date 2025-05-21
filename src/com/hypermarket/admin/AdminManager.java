package com.hypermarket.admin;

import com.hypermarket.user.User;
import com.hypermarket.user.UserManager;
import com.hypermarket.util.FileUtil;
import java.util.List;

public class AdminManager {
    private final UserManager userManager;

    // Constructor to initialize UserManager
    public AdminManager() {
        this.userManager = new UserManager();
    }

    // Method to check if the logged-in user is an admin
    private boolean isAdmin(User loggedInUser) {
        return loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("admin");
    }

    // Create a new user (admin-only)
    public boolean createUser(User loggedInUser, String username, String password, String role) {
        if (!isAdmin(loggedInUser)) {
            System.out.println("Access denied. Only admins can create users.");
            return false;
        }
        return userManager.createUser(username, password, role);
    }

    // Update a user's data by ID (admin-only)
    public boolean updateUser(User loggedInUser, int userId, String newUsername, String newPassword) {
        if (!isAdmin(loggedInUser)) {
            System.out.println("Access denied. Only admins can update users.");
            return false;
        }
        return userManager.updateUser(userId, newUsername, newPassword);
    }

    // Delete a user by ID (admin-only)
    public boolean deleteUser(User loggedInUser, int userId) {
        if (!isAdmin(loggedInUser)) {
            System.out.println("Access denied. Only admins can delete users.");
            return false;
        }

        List<User> users = FileUtil.readUsersFromFile();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                users.remove(i);
                FileUtil.writeUsersToFile(users);
                return true;
            }
        }
        System.out.println("User with ID " + userId + " not found.");
        return false;
    }

    // View all users (admin-only)
    public void viewAllUsers(User loggedInUser) {
        if (!isAdmin(loggedInUser)) {
            System.out.println("Access denied. Only admins can view all users.");
            return;
        }

        List<User> users = FileUtil.readUsersFromFile();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + ", Role: " + user.getRole());
        }
    }

    public List<User> getAllUsers(User loggedInUser) {
        return List.of();
    }
}