package com.budget.ui;

import com.budget.models.User;
import com.budget.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SideBar {
    private VBox view;
    private User currentUser;
    private Button dashboardBtn;
    private Button transactionsBtn;
    private Button budgetsBtn;
    private Button goalsBtn;
    private Button reportsBtn;
    private Button settingsBtn;
    private Button logoutBtn;

    public SideBar(User user) {
        this.currentUser = user;
        initializeView();
    }

    private void initializeView() {
        view = new VBox(12);
        view.setPadding(new Insets(25, 15, 25, 15));
        view.setPrefWidth(260);
        view.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #0f0f1a);");
        view.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));

        // Logo Section
        VBox logoBox = new VBox(5);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 0, 30, 0));

        Text logoIcon = new Text("💰");
        logoIcon.setFont(Font.font(40));
        Text logoText = new Text("Budget\nManager");
        logoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        logoText.setStyle("-fx-fill: white; -fx-text-alignment: center;");
        logoBox.getChildren().addAll(logoIcon, logoText);

        // User Info Section
        VBox userBox = new VBox(5);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(0, 0, 30, 0));
        Text userName = new Text(currentUser != null ? currentUser.getUsername() : "Guest");
        userName.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        userName.setStyle("-fx-fill: #b0c4de;");
        userBox.getChildren().add(userName);

        // Buttons
        dashboardBtn = createSidebarButton("📊", " Dashboard");
        transactionsBtn = createSidebarButton("💵", " Transactions");
        budgetsBtn = createSidebarButton("🎯", " Budgets");
        goalsBtn = createSidebarButton("🏆", " Goals");
        reportsBtn = createSidebarButton("📈", " Reports");
        settingsBtn = createSidebarButton("⚙️", " Settings");
        logoutBtn = createSidebarButton("🚪", " Logout");

        // Button Actions
        dashboardBtn.setOnAction(e -> MainApp.showDashboard());
        transactionsBtn.setOnAction(e -> MainApp.showTransactions());
        budgetsBtn.setOnAction(e -> MainApp.showBudgets());
        goalsBtn.setOnAction(e -> MainApp.showGoals());
        reportsBtn.setOnAction(e -> MainApp.showReports());
        settingsBtn.setOnAction(e -> MainApp.showSettings());
        logoutBtn.setOnAction(e -> {
            AuthService authService = new AuthService();
            authService.logout();
            MainApp.showLoginScreen();
        });

        // Set Dashboard as active by default
        setActiveButton(dashboardBtn);

        view.getChildren().addAll(logoBox, userBox,
                dashboardBtn, transactionsBtn,
                budgetsBtn, goalsBtn, reportsBtn,
                new VBox(20), settingsBtn, logoutBtn);
    }

    private Button createSidebarButton(String icon, String text) {
        Button btn = new Button(icon + text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #b0c4de; -fx-font-size: 14px; " +
                "-fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 10 15;");

        // Hover effect
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains("#3498db")) {
                btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 10 15;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains("#3498db")) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #b0c4de; -fx-font-size: 14px; " +
                        "-fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 10 15;");
            }
        });

        return btn;
    }

    public void setActiveButton(Button activeBtn) {
        String normalStyle = "-fx-background-color: transparent; -fx-text-fill: #b0c4de; -fx-font-size: 14px; " +
                "-fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 10 15;";
        String activeStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 10 15;";

        dashboardBtn.setStyle(normalStyle);
        transactionsBtn.setStyle(normalStyle);
        budgetsBtn.setStyle(normalStyle);
        goalsBtn.setStyle(normalStyle);
        reportsBtn.setStyle(normalStyle);
        settingsBtn.setStyle(normalStyle);
        logoutBtn.setStyle(normalStyle);

        activeBtn.setStyle(activeStyle);
    }

    public VBox getView() {
        return view;
    }
}