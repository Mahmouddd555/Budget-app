package com.budget.ui;

import com.budget.ui.MainApp;
import com.budget.database.DatabaseInitializer;
import com.budget.models.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;

public class MainApp extends Application {
    private static Stage primaryStage;
    private static BorderPane rootLayout;
    private static User currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("💰 Budget Manager Pro");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(750);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.initStyle(StageStyle.DECORATED);

        // Initialize database
        DatabaseInitializer.initializeDatabase();

        // Show login screen
        showLoginScreen();

        primaryStage.show();
    }

    public static void showLoginScreen() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 1300, 750);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    public static void showRegisterScreen() {
        RegisterView registerView = new RegisterView();
        Scene scene = new Scene(registerView.getView(), 1300, 750);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    public static void showMainView(User user) {
        currentUser = user;
        rootLayout = new BorderPane();

        SideBar sideBar = new SideBar(user);
        rootLayout.setLeft(sideBar.getView());

        TopNavBar topNavBar = new TopNavBar(user);
        rootLayout.setTop(topNavBar.getView());

        DashboardView dashboard = new DashboardView(user);
        rootLayout.setCenter(dashboard.getView());

        Scene scene = new Scene(rootLayout, 1300, 750);
        loadCSS(scene);
        primaryStage.setScene(scene);

        // أضف السطر ده بالضبط 👇
        ThemeManager.init(scene, rootLayout);
    }

    public static void showDashboard() {
        DashboardView dashboard = new DashboardView(currentUser);
        rootLayout.setCenter(dashboard.getView());
        updateTopNavBarTitle("Dashboard");
    }

    public static void refreshDashboard() {
        if (currentUser != null && rootLayout.getCenter() != null) {
            DashboardView newDashboard = new DashboardView(currentUser);
            rootLayout.setCenter(newDashboard.getView());
        }
    }

    public static void showTransactions() {
        TransactionsView transactions = new TransactionsView(currentUser);
        rootLayout.setCenter(transactions.getView());
        updateTopNavBarTitle("Transactions");
    }

    public static void showBudgets() {
        BudgetsView budgets = new BudgetsView(currentUser);
        rootLayout.setCenter(budgets.getView());
        updateTopNavBarTitle("Budgets");
    }

    public static void showGoals() {
        GoalsView goals = new GoalsView(currentUser);
        rootLayout.setCenter(goals.getView());
        updateTopNavBarTitle("Goals");
    }

    public static void showReports() {
        ReportsView reports = new ReportsView(currentUser);
        rootLayout.setCenter(reports.getView());
        updateTopNavBarTitle("Reports");
    }

    public static void showSettings() {
        SettingsView settings = new SettingsView(currentUser);
        rootLayout.setCenter(settings.getView());
        updateTopNavBarTitle("Settings");
    }

    private static void updateTopNavBarTitle(String title) {
        // تم التعليق مؤقتاً
    }

    private static void loadCSS(Scene scene) {
        try {
            String cssPath = "src/main/resources/css/styles.css";
            File cssFile = new File(cssPath);
            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
                System.out.println("✅ CSS loaded successfully!");
            } else {
                System.out.println("⚠️ CSS file not found at: " + cssPath);
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading CSS: " + e.getMessage());
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}