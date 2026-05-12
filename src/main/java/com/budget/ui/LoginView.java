package com.budget.ui;

import com.budget.service.AuthService;
import com.budget.models.User;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginView {
    private VBox view;
    private AuthService authService;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView() {
        this.authService = new AuthService();
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f0f1a);");

        // Logo and Title Section with Animation
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);

        Text logoIcon = new Text("💰");
        logoIcon.setFont(Font.font(70));

        Text title = new Text("Budget Manager Pro");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);

        Text subtitle = new Text("Take Control of Your Finances");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setFill(Color.web("#b0c4de"));

        headerBox.getChildren().addAll(logoIcon, title, subtitle);

        // Login Card
        VBox loginCard = new VBox(20);
        loginCard.setMaxWidth(420);
        loginCard.setPadding(new Insets(35));
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);");

        // Card Title
        Text loginTitle = new Text("Welcome Back!");
        loginTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        loginTitle.setFill(Color.web("#2c3e50"));

        Text loginSubtitle = new Text("Please enter your credentials");
        loginSubtitle.setFont(Font.font("Segoe UI", 13));
        loginSubtitle.setFill(Color.web("#7f8c8d"));

        VBox titleBox = new VBox(5, loginTitle, loginSubtitle);

        // Username Field with Icon
        usernameField = createTextField("👤", "Username");

        // Password Field with Icon
        passwordField = createPasswordField("🔒", "Password");

        // Login Button
        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> handleLogin());

        // Hover effect for button
        loginButton.setOnMouseEntered(e -> loginButton
                .setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;"));
        loginButton.setOnMouseExited(e -> loginButton
                .setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;"));

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        // Register Link
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #7f8c8d;");
        Hyperlink registerLink = new Hyperlink("Create one");
        registerLink.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        registerLink.setOnAction(e -> MainApp.showRegisterScreen());
        registerBox.getChildren().addAll(noAccountLabel, registerLink);

        // Forgot Password Link
        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        forgotLink.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 12px;");
        forgotLink.setAlignment(Pos.CENTER_RIGHT);
        forgotLink.setMaxWidth(Double.MAX_VALUE);
        forgotLink.setOnAction(e -> showForgotPasswordDialog());

        loginCard.getChildren().addAll(titleBox, usernameField, passwordField, loginButton, messageLabel, registerBox,
                forgotLink);

        // Fade animation for card
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), loginCard);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        view.getChildren().addAll(headerBox, loginCard);
    }

    private TextField createTextField(String icon, String placeholder) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 5 15;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle("-fx-background-color: transparent; -fx-padding: 12 0; -fx-font-size: 14px;");
        HBox.setHgrow(field, Priority.ALWAYS);

        box.getChildren().addAll(iconLabel, field);
        return field;
    }

    private PasswordField createPasswordField(String icon, String placeholder) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 5 15;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setStyle("-fx-background-color: transparent; -fx-padding: 12 0; -fx-font-size: 14px;");
        HBox.setHgrow(field, Priority.ALWAYS);

        box.getChildren().addAll(iconLabel, field);
        return field;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠️ Please fill in all fields");
            shakeField(messageLabel);
            return;
        }

        if (authService.login(username, password)) {
            User currentUser = authService.getCurrentUser();
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
            messageLabel.setText("✅ Login successful! Redirecting...");

            // Animate and redirect
            FadeTransition fade = new FadeTransition(Duration.millis(500), view);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(e -> MainApp.showMainView(currentUser));
            fade.play();
        } else {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("❌ Invalid username or password");
            shakeField(messageLabel);
        }
    }

    private void showForgotPasswordDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText(null);
        alert.setContentText("Please contact admin to reset your password.");
        alert.showAndWait();
    }

    private void shakeField(javafx.scene.Node node) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50),
                node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    public VBox getView() {
        return view;
    }
}