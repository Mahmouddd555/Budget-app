package com.budget.ui;

import com.budget.models.User;
import com.budget.models.Budget;
import com.budget.controller.BudgetController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.List;

public class BudgetsView {
    private VBox view;
    private User currentUser;
    private final BudgetController budgetController = new BudgetController();
    private TableView<Budget> tableView;
    private ObservableList<Budget> budgetList;
    private Label totalAllocatedLabel;
    private Label totalSpentLabel;
    private Label totalRemainingLabel;

    public BudgetsView(User user) {
        this.currentUser = user;
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("app-page");

        if (currentUser == null)
            return;

        // Header
        HBox header = createHeader();

        // Summary Cards
        GridPane summaryCards = createSummaryCards();

        // Table
        setupTableView();
        loadBudgets();

        // Action Bar
        HBox actionBar = createActionBar();

        view.getChildren().addAll(header, summaryCards, tableView, actionBar);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("🎯 Budget Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.getStyleClass().add("app-heading");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tipLabel = new Label("💡 Tip: Stay within budget to save more!");
        tipLabel.getStyleClass().add("app-chip");

        header.getChildren().addAll(title, spacer, tipLabel);
        return header;
    }

    private GridPane createSummaryCards() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 0, 20, 0));

        if (budgetList == null)
            budgetList = FXCollections.observableArrayList();

        double totalAllocated = budgetList.stream().mapToDouble(Budget::getAllocatedAmount).sum();
        double totalSpent = budgetList.stream().mapToDouble(Budget::getSpentAmount).sum();
        double totalRemaining = totalAllocated - totalSpent;

        VBox allocatedCard = createStatCard("💰 Total Allocated", String.format("$%.2f", totalAllocated), "#3498db");
        VBox spentCard = createStatCard("💸 Total Spent", String.format("$%.2f", totalSpent), "#e74c3c");
        VBox remainingCard = createStatCard("✅ Total Remaining", String.format("$%.2f", totalRemaining),
                totalRemaining >= 0 ? "#27ae60" : "#e74c3c");

        totalAllocatedLabel = (Label) ((VBox) allocatedCard.getChildren().get(1)).getChildren().get(0);
        totalSpentLabel = (Label) ((VBox) spentCard.getChildren().get(1)).getChildren().get(0);
        totalRemainingLabel = (Label) ((VBox) remainingCard.getChildren().get(1)).getChildren().get(0);

        grid.add(allocatedCard, 0, 0);
        grid.add(spentCard, 1, 0);
        grid.add(remainingCard, 2, 0);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            grid.getColumnConstraints().add(col);
        }

        return grid;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.getStyleClass().add("app-panel");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("app-muted");

        VBox valueBox = new VBox();
        valueBox.setAlignment(Pos.CENTER);
        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: %s;", color));
        valueBox.getChildren().add(valueLabel);

        card.getChildren().addAll(titleLabel, valueBox);
        return card;
    }

    private void setupTableView() {
        tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Budget, Integer> idCol = new TableColumn<>("#");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Budget, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);

        TableColumn<Budget, Double> allocatedCol = new TableColumn<>("Allocated");
        allocatedCol.setCellValueFactory(new PropertyValueFactory<>("allocatedAmount"));
        allocatedCol.setPrefWidth(120);
        allocatedCol.setCellFactory(col -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : String.format("$%.2f", amount));
            }
        });

        TableColumn<Budget, Double> spentCol = new TableColumn<>("Spent");
        spentCol.setCellValueFactory(new PropertyValueFactory<>("spentAmount"));
        spentCol.setPrefWidth(120);
        spentCol.setCellFactory(col -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : String.format("$%.2f", amount));
            }
        });

        TableColumn<Budget, Double> remainingCol = new TableColumn<>("Remaining");
        remainingCol.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
        remainingCol.setPrefWidth(120);
        remainingCol.setCellFactory(col -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null)
                    setText(null);
                else {
                    setText(String.format("$%.2f", amount));
                    setStyle(amount < 0 ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;"
                            : "-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Budget, String> periodCol = new TableColumn<>("Period");
        periodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
        periodCol.setPrefWidth(100);

        TableColumn<Budget, Double> usageCol = new TableColumn<>("Usage");
        usageCol.setCellValueFactory(new PropertyValueFactory<>("percentageUsed"));
        usageCol.setPrefWidth(180);
        usageCol.setCellFactory(col -> new TableCell<Budget, Double>() {
            private ProgressBar progressBar = new ProgressBar();

            @Override
            protected void updateItem(Double percentage, boolean empty) {
                super.updateItem(percentage, empty);
                if (empty || percentage == null)
                    setGraphic(null);
                else {
                    double progress = Math.min(percentage / 100.0, 1.0);
                    progressBar.setProgress(progress);
                    progressBar.setPrefWidth(100);
                    String color = percentage < 50 ? "#27ae60" : (percentage < 80 ? "#f39c12" : "#e74c3c");
                    progressBar.setStyle(String.format("-fx-accent: %s;", color));
                    HBox container = new HBox(10, progressBar, new Label(String.format("%.1f%%", percentage)));
                    container.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(container);
                }
            }
        });

        TableColumn<Budget, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(80);
        actionCol.setCellFactory(col -> new TableCell<Budget, Void>() {
            private final Button deleteBtn = new Button("🗑️");
            {
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-cursor: hand;");
                deleteBtn.setOnAction(e -> {
                    Budget b = getTableView().getItems().get(getIndex());
                    deleteBudget(b);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        tableView.getColumns().addAll(idCol, categoryCol, allocatedCol, spentCol, remainingCol, periodCol, usageCol,
                actionCol);
    }

    private void loadBudgets() {
        budgetController.refreshSpending(currentUser.getId(), "MONTHLY");
        budgetList = FXCollections.observableArrayList(budgetController.loadBudgetsForUser(currentUser.getId()));
        tableView.setItems(budgetList);

        // Update summary
        double totalAllocated = budgetList.stream().mapToDouble(Budget::getAllocatedAmount).sum();
        double totalSpent = budgetList.stream().mapToDouble(Budget::getSpentAmount).sum();
        totalAllocatedLabel.setText(String.format("$%.2f", totalAllocated));
        totalSpentLabel.setText(String.format("$%.2f", totalSpent));
        totalRemainingLabel.setText(String.format("$%.2f", totalAllocated - totalSpent));
    }

    private HBox createActionBar() {
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_RIGHT);

        Button addBtn = new Button("➕ Create Budget");
        addBtn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        addBtn.setOnAction(e -> showCreateBudgetDialog());

        actionBar.getChildren().add(addBtn);
        return actionBar;
    }

    private void showCreateBudgetDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Budget");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Set Budget Limit");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Transport", "Shopping", "Entertainment", "Bills", "Healthcare",
                "Education", "Other");
        categoryBox.setPromptText("Select Category");

        TextField amountField = new TextField();
        amountField.setPromptText("Budget Amount");

        ComboBox<String> periodBox = new ComboBox<>();
        periodBox.getItems().addAll("DAILY", "WEEKLY", "MONTHLY");
        periodBox.setValue("MONTHLY");

        content.getChildren().addAll(title, categoryBox, amountField, periodBox);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String category = categoryBox.getValue();
                    double amount = Double.parseDouble(amountField.getText());
                    String period = periodBox.getValue();
                    if (category != null && amount > 0) {
                        budgetController.createBudget(currentUser.getId(), category, amount, period);
                        loadBudgets();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteBudget(Budget budget) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Budget");
        alert.setContentText("Delete budget for " + budget.getCategory() + "?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                budgetController.deleteBudget(budget.getId());
                loadBudgets();
            }
        });
    }

    public VBox getView() {
        return view;
    }
}