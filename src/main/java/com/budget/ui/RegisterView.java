package com.budget.ui;


import com.budget.service.AuthService;
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


public class RegisterView {
    private VBox view;
    private AuthService authService;
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;
    private ProgressIndicator progressIndicator;

    public RegisterView() {
        this.authService = new AuthService();
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f0f1a);");

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Text logoIcon = new Text("📝");
        logoIcon.setFont(Font.font(60));

        Text title = new Text("Create Account");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);

        Text subtitle = new Text("Join us and start managing your finances");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setFill(Color.web("#b0c4de"));

        headerBox.getChildren().addAll(logoIcon, title, subtitle);

        // Register Card
        VBox registerCard = new VBox(20);
        registerCard.setMaxWidth(450);
        registerCard.setPadding(new Insets(35));
        registerCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);");

        // Card Title
        Text registerTitle = new Text("Sign Up");
        registerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        registerTitle.setFill(Color.web("#2c3e50"));

        Text registerSubtitle = new Text("Please fill in your information");
        registerSubtitle.setFont(Font.font("Segoe UI", 13));
        registerSubtitle.setFill(Color.web("#7f8c8d"));

        VBox titleBox = new VBox(5, registerTitle, registerSubtitle);

        // Form Fields
        usernameField = createTextField("👤", "Username");
        emailField = createTextField("📧", "Email");
        passwordField = createPasswordField("🔒", "Password");
        confirmPasswordField = createPasswordField("✓", "Confirm Password");

        // Password strength indicator
        ProgressBar strengthBar = new ProgressBar(0);
        strengthBar.setPrefWidth(Double.MAX_VALUE);
        strengthBar.setStyle("-fx-accent: #e74c3c;");
        Label strengthLabel = new Label("Password strength: Weak");
        strengthLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c;");

        passwordField.textProperty().addListener((obs, old, newVal) -> {
            int strength = calculatePasswordStrength(newVal);
            double progress = strength / 4.0;
            strengthBar.setProgress(progress);
            if (strength <= 1) {
                strengthBar.setStyle("-fx-accent: #e74c3c;");
                strengthLabel.setText("Password strength: Weak");
                strengthLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c;");
            } else if (strength <= 2) {
                strengthBar.setStyle("-fx-accent: #f39c12;");
                strengthLabel.setText("Password strength: Medium");
                strengthLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #f39c12;");
            } else {
                strengthBar.setStyle("-fx-accent: #27ae60;");
                strengthLabel.setText("Password strength: Strong");
                strengthLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60;");
            }
        });

        VBox passwordStrengthBox = new VBox(5, strengthBar, strengthLabel);

        // Register Button
        Button registerButton = new Button("Create Account");
        registerButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> handleRegister());

        registerButton.setOnMouseEntered(e -> registerButton
                .setStyle("-fx-background-color: #219a52; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;"));
        registerButton.setOnMouseExited(e -> registerButton
                .setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;"));

        // Progress Indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(30, 30);

        HBox buttonBox = new HBox(10, registerButton, progressIndicator);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMaxWidth(Double.MAX_VALUE);

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        // Login Link
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label haveAccountLabel = new Label("Already have an account?");
        haveAccountLabel.setStyle("-fx-text-fill: #7f8c8d;");
        Hyperlink loginLink = new Hyperlink("Sign In");
        loginLink.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        loginLink.setOnAction(e -> MainApp.showLoginScreen());
        loginBox.getChildren().addAll(haveAccountLabel, loginLink);

        registerCard.getChildren().addAll(titleBox, usernameField, emailField, passwordField,
                confirmPasswordField, passwordStrengthBox, buttonBox,
                messageLabel, loginBox);

        // Fade animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), registerCard);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        view.getChildren().addAll(headerBox, registerCard);
    }

    private TextField createTextField(String icon, String placeholder) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 5 15;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");

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
        iconLabel.setStyle("-fx-font-size: 16px;");

        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setStyle("-fx-background-color: transparent; -fx-padding: 12 0; -fx-font-size: 14px;");
        HBox.setHgrow(field, Priority.ALWAYS);

        box.getChildren().addAll(iconLabel, field);
        return field;
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8)
            strength++;
        if (password.matches(".*[A-Z].*"))
            strength++;
        if (password.matches(".*[a-z].*"))
            strength++;
        if (password.matches(".*\\d.*"))
            strength++;
        if (password.matches(".*[!@#$%^&*].*"))
            strength++;
        return Math.min(strength, 4);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠️ Please fill in all fields");
            return;
        }

        if (!password.equals(confirm)) {
            messageLabel.setText("⚠️ Passwords do not match");
            return;
        }

        if (password.length() < 4) {
            messageLabel.setText("⚠️ Password must be at least 4 characters");
            return;
        }

        boolean success = authService.register(username, email, password);

        if (success) {
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
            messageLabel.setText("✅ Registration successful! Redirecting...");
            MainApp.showLoginScreen();
        } else {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("❌ Username or email already exists!");
        }
    }

    private void shakeField(javafx.scene.Node field) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50),
                field);
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