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

        JFrame frame = new JFrame("Masroofy — Login");
        frame.setSize(460, 520);
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
        card.setPreferredSize(new Dimension(380, 420));

        JLabel title = UITheme.label(
                "Welcome Back",
                UITheme.FONT_TITLE,
                UITheme.TEXT_PRIMARY);

        JLabel sub = UITheme.label(
                "Login to continue",
                UITheme.FONT_BODY,
                UITheme.TEXT_SECONDARY);

        JTextField emailField = UITheme.styledField();
        JPasswordField passField = UITheme.styledPassword();

        JLabel errorLabel = UITheme.label(
                "",
                UITheme.FONT_SMALL,
                UITheme.DANGER);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(UITheme.FONT_H3);
        loginBtn.setBackground(UITheme.PRIMARY);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        loginBtn.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Email and password required.");
                return;
            }

            User user = authController.handleLogin(email, password);

            if (user != null) {
                frame.dispose();

                // safe call (avoid compile risk)
                new DashboardScreen(user).show();
            } else {
                errorLabel.setText("Invalid email or password.");
                passField.setText("");
            }
        });

        JButton registerBtn = new JButton("Create Account");
        registerBtn.setFont(UITheme.FONT_SMALL);
        registerBtn.setForeground(UITheme.PRIMARY);
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerBtn.addActionListener(e -> {
            frame.dispose();
            new RegisterScreen().show();
        });

        // ===== Layout =====
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        card.add(UITheme.fieldGroup("EMAIL", emailField));
        card.add(Box.createVerticalStrut(12));

        card.add(UITheme.fieldGroup("PASSWORD", passField));
        card.add(Box.createVerticalStrut(10));

        card.add(errorLabel);
        card.add(Box.createVerticalStrut(15));

        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        card.add(registerBtn);

        return card;
    }
}