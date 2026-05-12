package com.budget.service;

import com.budget.database.UserDAO;
import com.budget.models.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {
    private UserDAO userDAO;
    private User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String username, String email, String password) {
        // Check if user already exists
        if (userDAO.userExists(username, email)) {
            System.out.println("User already exists: " + username);
            return false;
        }

        String passwordHash = hashPassword(password);
        User newUser = new User(username, email, passwordHash);
        boolean created = userDAO.createUser(newUser);

        if (created) {
            System.out.println("✅ User created successfully: " + username);
        } else {
            System.out.println("❌ Failed to create user: " + username);
        }

        return created;
    }

    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && verifyPassword(password, user.getPasswordHash())) {
            currentUser = user;
            System.out.println("✅ Login successful: " + username);
            return true;
        }
        System.out.println("❌ Login failed: " + username);
        return false;
    }

    public void logout() {
        currentUser = null;
        System.out.println("User logged out");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
}