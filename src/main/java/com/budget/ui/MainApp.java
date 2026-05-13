package com.budget.ui;

import com.budget.database.DatabaseInitializer;
import com.budget.database.UserDAO;
import com.budget.models.User;
import com.budget.util.CssResources;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX entry point and coarse navigation. Keeps the logged-in {@link User} in sync with the database
 * after mutations (balance, etc.).
 */
public class MainApp extends Application {

    public static final String VIEW_DASHBOARD = "DASHBOARD";
    public static final String VIEW_TRANSACTIONS = "TRANSACTIONS";
    public static final String VIEW_BUDGETS = "BUDGETS";
    public static final String VIEW_GOALS = "GOALS";
    public static final String VIEW_REPORTS = "REPORTS";
    public static final String VIEW_SETTINGS = "SETTINGS";

    private static Stage primaryStage;
    private static BorderPane rootLayout;
    private static User currentUser;
    private static TopNavBar topNavBarInstance;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Budget Manager Pro");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(750);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.initStyle(StageStyle.DECORATED);

        DatabaseInitializer.initializeDatabase();
        showLoginScreen();
        primaryStage.show();
    }

    public static void showLoginScreen() {
        currentUser = null;
        topNavBarInstance = null;
        rootLayout = null;
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 1300, 750);
        applyAuthSceneStylesheets(scene);
        primaryStage.setScene(scene);
    }

    public static void showRegisterScreen() {
        RegisterView registerView = new RegisterView();
        Scene scene = new Scene(registerView.getView(), 1300, 750);
        applyAuthSceneStylesheets(scene);
        primaryStage.setScene(scene);
    }

    private static void applyAuthSceneStylesheets(Scene scene) {
        scene.getStylesheets().clear();
        String light = CssResources.lightThemeExternalForm();
        if (light != null) {
            scene.getStylesheets().add(light);
        }
    }

    public static void showMainView(User user) {
        currentUser = user;
        rootLayout = new BorderPane();

        SideBar sideBar = new SideBar(user);
        rootLayout.setLeft(sideBar.getView());

        topNavBarInstance = new TopNavBar(user);
        rootLayout.setTop(topNavBarInstance.getView());

        DashboardView dashboard = new DashboardView(currentUser);
        Node center = dashboard.getView();
        center.setUserData(VIEW_DASHBOARD);
        rootLayout.setCenter(center);

        Scene scene = new Scene(rootLayout, 1300, 750);
        applyAuthSceneStylesheets(scene);
        primaryStage.setScene(scene);

        ThemeManager.init(scene, rootLayout);
        SettingsManager.init(scene);
    }

    private static void setCenterWithId(Node node, String viewId) {
        node.setUserData(viewId);
        rootLayout.setCenter(node);
    }

    public static void showDashboard() {
        setCenterWithId(new DashboardView(currentUser).getView(), VIEW_DASHBOARD);
        updateTopNavBarTitle("Dashboard");
    }

    /** Rebuilds the dashboard only if it is the active center view. */
    public static void refreshDashboardIfVisible() {
        if (currentUser == null || rootLayout == null) {
            return;
        }
        Node center = rootLayout.getCenter();
        if (center != null && VIEW_DASHBOARD.equals(center.getUserData())) {
            setCenterWithId(new DashboardView(currentUser).getView(), VIEW_DASHBOARD);
        }
    }

    /**
     * Reloads {@link #currentUser} from the database so balance and aggregates stay consistent with SQLite.
     */
    public static void syncCurrentUserFromDatabase() {
        if (currentUser == null) {
            return;
        }
        User fresh = new UserDAO().getUserById(currentUser.getId());
        if (fresh == null) {
            return;
        }
        currentUser.setTotalBalance(fresh.getTotalBalance());
        currentUser.setMonthlyIncome(fresh.getMonthlyIncome());
    }

    /**
     * Call after any transaction or other change that affects balances. Updates top bar and dashboard if visible.
     * Does not switch the active screen away from Transactions/Budgets/etc.
     */
    public static void refreshAfterDataChange() {
        syncCurrentUserFromDatabase();
        if (topNavBarInstance != null && currentUser != null) {
            topNavBarInstance.updateBalance(currentUser.getTotalBalance());
        }
        refreshDashboardIfVisible();
        ThemeManager.refreshCurrentView();
    }

    public static void showTransactions() {
        setCenterWithId(new TransactionsView(currentUser).getView(), VIEW_TRANSACTIONS);
        updateTopNavBarTitle("Transactions");
    }

    public static void showBudgets() {
        setCenterWithId(new BudgetsView(currentUser).getView(), VIEW_BUDGETS);
        updateTopNavBarTitle("Budgets");
    }

    public static void showGoals() {
        setCenterWithId(new GoalsView(currentUser).getView(), VIEW_GOALS);
        updateTopNavBarTitle("Goals");
    }

    public static void showReports() {
        setCenterWithId(new ReportsView(currentUser).getView(), VIEW_REPORTS);
        updateTopNavBarTitle("Reports");
    }

    public static void showSettings() {
        setCenterWithId(new SettingsView(currentUser).getView(), VIEW_SETTINGS);
        updateTopNavBarTitle("Settings");
    }

    private static void updateTopNavBarTitle(String title) {
        if (topNavBarInstance != null) {
            topNavBarInstance.updateTitle(title);
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
