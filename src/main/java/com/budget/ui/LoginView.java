package com.budget.ui;

import com.budget.controller.LoginController;
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

/**
 * Login layout only; delegates validation and authentication to {@link LoginController}.
 */
public class LoginView {
    private VBox view;
    private final LoginController loginController = new LoginController();
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView() {
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f0f1a);");

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

        VBox loginCard = new VBox(20);
        loginCard.setMaxWidth(420);
        loginCard.setPadding(new Insets(35));
        loginCard.getStyleClass().add("login-card");

        Text loginTitle = new Text("Welcome Back!");
        loginTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        loginTitle.setFill(Color.web("#2c3e50"));

        Text loginSubtitle = new Text("Please enter your credentials");
        loginSubtitle.setFont(Font.font("Segoe UI", 13));
        loginSubtitle.setFill(Color.web("#7f8c8d"));

        VBox titleBox = new VBox(5, loginTitle, loginSubtitle);

        usernameField = createTextField("👤", "Username");
        passwordField = createPasswordField("🔒", "Password");

        Button loginButton = new Button("Sign In");
        loginButton.getStyleClass().addAll("btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> handleLogin());

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #7f8c8d;");
        Hyperlink registerLink = new Hyperlink("Create one");
        registerLink.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        registerLink.setOnAction(e -> MainApp.showRegisterScreen());
        registerBox.getChildren().addAll(noAccountLabel, registerLink);

        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        forgotLink.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 12px;");
        forgotLink.setAlignment(Pos.CENTER_RIGHT);
        forgotLink.setMaxWidth(Double.MAX_VALUE);
        forgotLink.setOnAction(e -> showForgotPasswordDialog());

        loginCard.getChildren().addAll(titleBox, usernameField, passwordField, loginButton, messageLabel, registerBox,
                forgotLink);

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
        LoginController.AuthResult result = loginController.login(usernameField.getText(), passwordField.getText());

        if (!result.isSuccess()) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText(result.getMessage() != null ? "⚠️ " + result.getMessage() : "⚠️ Login failed");
            shakeField(messageLabel);
            return;
        }

        User currentUser = result.getUser();
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("Login successful! Redirecting...");

        FadeTransition fade = new FadeTransition(Duration.millis(500), view);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> MainApp.showMainView(currentUser));
        fade.play();
    }

    private void showForgotPasswordDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText(null);
        alert.setContentText("Please contact admin to reset your password.");
        alert.showAndWait();
    }

    private void shakeField(javafx.scene.Node node) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50), node);
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
