package ui;

import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen {

    private final AuthController authController;

    public RegisterScreen() {
        this.authController = new AuthController();
    }

    public void show() {
        JFrame frame = new JFrame("Finance Manager — Register");
        frame.setSize(400, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        JTextField nameField     = new JTextField();
        JTextField emailField    = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confField = new JPasswordField();
        JComboBox<String> currencyBox = new JComboBox<>(new String[]{"USD", "EUR", "GBP"});
        JComboBox<String> langBox     = new JComboBox<>(new String[]{"en", "ar"});
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(new JLabel("Name:"));        panel.add(nameField);
        panel.add(new JLabel("Email:"));       panel.add(emailField);
        panel.add(new JLabel("Password:"));    panel.add(passField);
        panel.add(new JLabel("Confirm Pass:")); panel.add(confField);
        panel.add(new JLabel("Currency:"));    panel.add(currencyBox);
        panel.add(new JLabel("Language:"));    panel.add(langBox);
        panel.add(new JLabel(""));             panel.add(new JLabel(""));

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String name     = nameField.getText().trim();
            String email    = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String confirm  = new String(confField.getPassword()).trim();
            String currency = (String) currencyBox.getSelectedItem();
            String language = (String) langBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("All fields are required.");
                return;
            }
            if (!password.equals(confirm)) {
                errorLabel.setText("Passwords do not match.");
                return;
            }
            if (password.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters.");
                return;
            }

            User user = authController.handleRegister(name, email, password, currency, language);
            if (user != null) {
                JOptionPane.showMessageDialog(frame,
                        "Registration successful! Please login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new LoginScreen().show();
            } else {
                errorLabel.setText("Email already exists.");
            }
        });

        panel.add(registerBtn);
        panel.add(errorLabel);

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void navigateToLogin() {
        new LoginScreen().show();
    }
}