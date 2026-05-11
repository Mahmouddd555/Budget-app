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

        JFrame frame = new JFrame("Masroofy — Create Account");
        frame.setSize(480, 640);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(UITheme.BG);
        frame.setLayout(new GridBagLayout());

        frame.add(buildCard(frame));

        frame.setVisible(true);
    }

    private JPanel buildCard(JFrame frame) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.SURFACE);
        card.setBorder(UITheme.cardBorder());
        card.setPreferredSize(new Dimension(400, 560));

        // ===== Title =====
        JLabel title = UITheme.label(
                "Create Account",
                UITheme.FONT_TITLE,
                UITheme.TEXT_PRIMARY);

        JLabel sub = UITheme.label(
                "Start managing your finances",
                UITheme.FONT_BODY,
                UITheme.TEXT_SECONDARY);

        // ===== Fields =====
        JTextField nameField = UITheme.styledField();
        JTextField emailField = UITheme.styledField();
        JPasswordField passField = UITheme.styledPassword();
        JPasswordField confField = UITheme.styledPassword();

        JComboBox<String> currencyBox = UITheme.styledCombo(new String[] { "EGP", "USD", "EUR" });

        JComboBox<String> langBox = UITheme.styledCombo(new String[] { "English", "Arabic" });

        // ===== Error =====
        JLabel errorLabel = UITheme.label("", UITheme.FONT_SMALL, UITheme.DANGER);

        // ===== Button =====
        JButton registerBtn = new JButton("Create Account");
        registerBtn.setFont(UITheme.FONT_H3);
        registerBtn.setBackground(UITheme.PRIMARY);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        registerBtn.addActionListener(e -> {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String confirm = new String(confField.getPassword()).trim();
            String currency = (String) currencyBox.getSelectedItem();
            String language = langBox.getSelectedIndex() == 0 ? "en" : "ar";

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

            User user = authController.handleRegister(
                    name, email, password, currency, language);

            if (user != null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
                new LoginScreen().show();
            } else {
                errorLabel.setText("Email already exists.");
            }
        });

        JButton loginLink = new JButton("Already have an account? Login");
        loginLink.setFont(UITheme.FONT_SMALL);
        loginLink.setForeground(UITheme.PRIMARY);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLink.addActionListener(e -> {
            frame.dispose();
            new LoginScreen().show();
        });

        // ===== Layout =====
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        card.add(UITheme.fieldGroup("Name", nameField));
        card.add(Box.createVerticalStrut(10));

        card.add(UITheme.fieldGroup("Email", emailField));
        card.add(Box.createVerticalStrut(10));

        card.add(UITheme.fieldGroup("Password", passField));
        card.add(Box.createVerticalStrut(10));

        card.add(UITheme.fieldGroup("Confirm Password", confField));
        card.add(Box.createVerticalStrut(10));

        card.add(UITheme.fieldGroup("Currency", currencyBox));
        card.add(Box.createVerticalStrut(10));

        card.add(UITheme.fieldGroup("Language", langBox));
        card.add(Box.createVerticalStrut(10));

        card.add(errorLabel);
        card.add(Box.createVerticalStrut(15));

        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));

        card.add(loginLink);

        return card;
    }
}