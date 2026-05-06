package ui;

import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen {

    private final AuthController authController;

    public LoginScreen() {
        this.authController = new AuthController();
    }

    public void show() {
        JFrame frame = new JFrame("Finance Manager — Login");
        frame.setSize(380, 320);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextField emailField    = new JTextField();
        JPasswordField passField = new JPasswordField();
        JLabel errorLabel        = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> {
            String email    = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Email and password are required.");
                return;
            }

            User user = authController.handleLogin(email, password);
            if (user != null) {
                frame.dispose();
                new DashboardScreen(user).show();
            } else {
                errorLabel.setText("Invalid email or password.");
                passField.setText("");
            }
        });

        JButton registerBtn = new JButton("Create Account");
        registerBtn.addActionListener(e -> {
            frame.dispose();
            new RegisterScreen().show();
        });

        panel.add(loginBtn);
        panel.add(errorLabel);
        panel.add(registerBtn);

        JLabel title = new JLabel("Personal Finance Manager", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}