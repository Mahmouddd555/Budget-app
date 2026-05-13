package com.budget.ui;

import com.budget.models.User;
import com.budget.models.Transaction;
import com.budget.service.TransactionService;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;

public class DashboardView {
    private VBox view;
    private User currentUser;
    private TransactionService transactionService;
    private Label balanceCardValue;
    private Label incomeCardValue;
    private Label expenseCardValue;
    private Label savingsCardValue;

    public DashboardView(User user) {
        this.currentUser = user;
        this.transactionService = new TransactionService();
        initializeView();
    }

    private void initializeView() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("app-scroll");

        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("app-page");

        if (currentUser == null) {
            Label errorLabel = new Label("❌ User not logged in!");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            view.getChildren().add(errorLabel);
            scrollPane.setContent(view);
            return;
        }

        // Welcome Section with animation
        Label welcomeLabel = new Label("Welcome back, " + currentUser.getUsername() + "! 👋");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        welcomeLabel.getStyleClass().add("app-title-welcome");
        FadeTransition fade = new FadeTransition(Duration.millis(500), welcomeLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // Stats Cards
        GridPane statsGrid = createStatsCards();

        // Charts Row
        HBox chartsRow = createChartsRow();

        // Recent Transactions
        VBox recentTransactions = createRecentTransactionsSection();

        view.getChildren().addAll(welcomeLabel, statsGrid, chartsRow, recentTransactions);
        scrollPane.setContent(view);
    }

    private GridPane createStatsCards() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 0, 20, 0));

        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        double totalIncome = transactionService.getTotalIncome(currentUser.getId(), startOfMonth, now);
        double totalExpense = transactionService.getTotalExpense(currentUser.getId(), startOfMonth, now);
        double balance = currentUser.getTotalBalance();
        double netSavings = totalIncome - totalExpense;

        VBox balanceCard = createStatCard("💰 Total Balance", String.format("$%.2f", balance),
                balance >= 0 ? "#27ae60" : "#e74c3c");
        VBox incomeCard = createStatCard("📈 Monthly Income", String.format("$%.2f", totalIncome), "#2980b9");
        VBox expenseCard = createStatCard("📉 Monthly Expenses", String.format("$%.2f", totalExpense), "#c0392b");
        VBox savingsCard = createStatCard("💎 Net Savings", String.format("$%.2f", netSavings),
                netSavings >= 0 ? "#27ae60" : "#e74c3c");

        grid.add(balanceCard, 0, 0);
        grid.add(incomeCard, 1, 0);
        grid.add(expenseCard, 2, 0);
        grid.add(savingsCard, 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }

        return grid;
    }

    private VBox createStatCard(String title, String value, String textColor) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefHeight(130);
        card.getStyleClass().add("app-panel");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("app-muted");

        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: %s;", textColor));

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    private HBox createChartsRow() {
        HBox chartsBox = new HBox(20);
        chartsBox.setPadding(new Insets(10, 0, 20, 0));

        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        // Pie Chart - Spending by Category
        VBox pieChartBox = createPieChart();

        // Bar Chart - Monthly comparison
        VBox barChartBox = createBarChart();

        chartsBox.getChildren().addAll(pieChartBox, barChartBox);
        HBox.setHgrow(pieChartBox, Priority.ALWAYS);
        HBox.setHgrow(barChartBox, Priority.ALWAYS);

        return chartsBox;
    }

    private VBox createPieChart() {
        VBox container = new VBox(10);
        container.getStyleClass().add("app-panel");

        Label title = new Label("📊 Spending by Category");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.getStyleClass().add("app-section-title");

        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        Map<String, Double> spendingByCategory = transactionService.getSpendingByCategory(currentUser.getId(),
                startOfMonth, now);

        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(300, 250);
        pieChart.setLabelsVisible(true);

        if (spendingByCategory.isEmpty()) {
            PieChart.Data dummy = new PieChart.Data("No Data", 1);
            pieChart.getData().add(dummy);
        } else {
            for (Map.Entry<String, Double> entry : spendingByCategory.entrySet()) {
                PieChart.Data slice = new PieChart.Data(
                        entry.getKey() + " ($" + String.format("%.0f", entry.getValue()) + ")", entry.getValue());
                pieChart.getData().add(slice);
            }
        }

        container.getChildren().addAll(title, pieChart);
        return container;
    }

    private VBox createBarChart() {
        VBox container = new VBox(10);
        container.getStyleClass().add("app-panel");

        Label title = new Label("📈 Monthly Overview");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.getStyleClass().add("app-section-title");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(350, 250);
        barChart.setLegendVisible(true);
        barChart.setAnimated(false);

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        // Get last 3 months data
        LocalDate now = LocalDate.now();
        for (int i = 2; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            String monthName = monthDate.format(DateTimeFormatter.ofPattern("MMM"));
            LocalDate startOfMonth = monthDate.withDayOfMonth(1);
            LocalDate endOfMonth = monthDate.withDayOfMonth(monthDate.lengthOfMonth());

            double income = transactionService.getTotalIncome(currentUser.getId(), startOfMonth, endOfMonth);
            double expense = transactionService.getTotalExpense(currentUser.getId(), startOfMonth, endOfMonth);

            incomeSeries.getData().add(new XYChart.Data<>(monthName, income));
            expenseSeries.getData().add(new XYChart.Data<>(monthName, expense));
        }

        barChart.getData().addAll(incomeSeries, expenseSeries);
        container.getChildren().addAll(title, barChart);
        return container;
    }

    private VBox createRecentTransactionsSection() {
        VBox section = new VBox(15);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label sectionTitle = new Label("📝 Recent Transactions");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sectionTitle.getStyleClass().add("app-section-title");

        Button viewAllBtn = new Button("View All →");
        viewAllBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-cursor: hand; -fx-font-weight: bold;");
        viewAllBtn.setOnAction(e -> MainApp.showTransactions());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(sectionTitle, spacer, viewAllBtn);

        VBox transactionsList = new VBox(8);
        transactionsList.getStyleClass().add("app-panel");

        List<Transaction> transactions = transactionService.getUserTransactions(currentUser.getId());

        if (transactions.isEmpty()) {
            Label noTransactions = new Label("✨ No transactions yet. Click on 'Transactions' to add some!");
            noTransactions.setStyle("-fx-text-fill: #999; -fx-font-size: 14px; -fx-padding: 20;");
            noTransactions.setAlignment(Pos.CENTER);
            transactionsList.getChildren().add(noTransactions);
        } else {
            int count = Math.min(6, transactions.size());
            for (int i = 0; i < count; i++) {
                Transaction t = transactions.get(i);
                HBox row = createTransactionRow(t);
                transactionsList.getChildren().add(row);
                if (i < count - 1) {
                    Separator sep = new Separator();
                    sep.setStyle("-fx-background-color: #ecf0f1;");
                    transactionsList.getChildren().add(sep);
                }
            }
        }

        section.getChildren().addAll(header, transactionsList);
        return section;
    }

    private HBox createTransactionRow(Transaction t) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));
        HBox.setHgrow(row, Priority.ALWAYS);

        String typeIcon = t.getType().equals("INCOME") ? "💰" : "💸";
        Label iconLabel = new Label(typeIcon);
        iconLabel.setStyle("-fx-font-size: 20px; -fx-min-width: 40;");

        VBox infoBox = new VBox(3);
        Label categoryLabel = new Label(t.getCategory());
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 14px;");
        Label descLabel = new Label(t.getDescription());
        descLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        infoBox.getChildren().addAll(categoryLabel, descLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        VBox amountBox = new VBox(3);
        String amountColor = t.getType().equals("INCOME") ? "#27ae60" : "#e74c3c";
        String amountSign = t.getType().equals("INCOME") ? "+" : "-";
        Label amountLabel = new Label(String.format("%s$%.2f", amountSign, t.getAmount()));
        amountLabel
                .setStyle(String.format("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: %s;", amountColor));
        Label dateLabel = new Label(t.getDate().toString());
        dateLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");
        amountBox.getChildren().addAll(amountLabel, dateLabel);
        amountBox.setAlignment(Pos.CENTER_RIGHT);

        row.getChildren().addAll(iconLabel, infoBox, amountBox);

        // Hover effect
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent;"));

        return row;
    }

    public VBox getView() {
        return view;
    }
}