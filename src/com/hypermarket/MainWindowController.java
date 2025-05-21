package com.hypermarket;

import com.hypermarket.user.User;
import com.hypermarket.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MainWindowController {

    @FXML private Button loginButton;
    @FXML private Button createUserButton;
    @FXML private Button updateDataButton;
    @FXML private Button logoutButton;
    @FXML private Button exitButton;

    private UserManager userManager = new UserManager();
    private User loggedInUser = null;

    @FXML
    private void handleLogin() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setHeaderText("Login - Enter Username:");
        Optional<String> usernameResult = usernameDialog.showAndWait();

        if (usernameResult.isPresent()) {
            String username = usernameResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setHeaderText("Login - Enter Password:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();
                loggedInUser = userManager.login(username, password);
                if (loggedInUser != null) {
                    showMessage("Login successful! Welcome, " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
                    switchToLoggedInMode();
                } else {
                    showMessage("Invalid username or password.");
                }
            }
        }
    }

    @FXML
    private void handleCreateUser() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setHeaderText("Create User - Enter New Username:");
        Optional<String> usernameResult = usernameDialog.showAndWait();

        if (usernameResult.isPresent()) {
            String username = usernameResult.get();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setHeaderText("Create User - Enter New Password:");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                TextInputDialog roleDialog = new TextInputDialog();
                roleDialog.setHeaderText("Create User - Enter Role (admin/user):");
                Optional<String> roleResult = roleDialog.showAndWait();

                if (roleResult.isPresent()) {
                    String role = roleResult.get();
                    boolean created = userManager.createUser(username, password, role);
                    if (created) {
                        showMessage("User created successfully.");
                    } else {
                        showMessage("Username already exists. Try a different username.");
                    }
                }
            }
        }
    }

    @FXML
    private void handleUpdateData() {
        if (loggedInUser != null) {
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setHeaderText("Update - Enter New Username (leave empty to keep current):");
            Optional<String> usernameResult = usernameDialog.showAndWait();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setHeaderText("Update - Enter New Password (leave empty to keep current):");
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (usernameResult.isPresent() && passwordResult.isPresent()) {
                String newUsername = usernameResult.get();
                String newPassword = passwordResult.get();
                boolean updated = userManager.updateOwnData(loggedInUser, newUsername, newPassword);
                if (updated) {
                    showMessage("Your data has been updated successfully.");
                } else {
                    showMessage("Failed to update data.");
                }
            }
        }
    }

    @FXML
    private void handleLogout() {
        loggedInUser = null;
        showMessage("You have logged out successfully.");
        switchToLoggedOutMode();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void showMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToLoggedInMode() {
        loginButton.setVisible(false);
        createUserButton.setVisible(false);
        updateDataButton.setVisible(true);
        logoutButton.setVisible(true);
    }

    private void switchToLoggedOutMode() {
        loginButton.setVisible(true);
        createUserButton.setVisible(true);
        updateDataButton.setVisible(false);
        logoutButton.setVisible(false);
    }
}
