package com.budget.ui;

import com.budget.models.User;
import com.budget.models.Transaction;
import com.budget.controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionsView {
    private VBox view;
    private User currentUser;
    private final TransactionController transactionController = new TransactionController();
    private TableView<Transaction> tableView;
    private ObservableList<Transaction> transactionList;
    private ComboBox<String> periodFilter;
    private ComboBox<String> typeFilter;
    private TextField searchField;
    private Label totalIncomeLabel;
    private Label totalExpenseLabel;
    private Label netLabel;

    public TransactionsView(User user) {
        this.currentUser = user;
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("app-page");

        if (currentUser == null)
            return;

        HBox header = createHeader();
        HBox filterBar = createFilterBar();
        HBox summaryBar = createSummaryBar(); // <-- حركتها هنا (قبل setupTableView)
        setupTableView();
        loadTransactions();
        HBox actionBar = createActionBar();

        view.getChildren().addAll(header, filterBar, tableView, summaryBar, actionBar);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("💰 Transactions");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.getStyleClass().add("app-heading");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statsLabel = new Label("📊 All your financial activity");
        statsLabel.getStyleClass().add("app-chip");

        header.getChildren().addAll(title, spacer, statsLabel);
        return header;
    }

    private HBox createFilterBar() {
        HBox filterBar = new HBox(15);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("Filter:");
        filterLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        periodFilter = new ComboBox<>();
        periodFilter.getItems().addAll("All Time", "Today", "This Week", "This Month", "This Year");
        periodFilter.setValue("This Month");
        periodFilter.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        periodFilter.setOnAction(e -> loadTransactions());

        typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All", "Income", "Expense");
        typeFilter.setValue("All");
        typeFilter.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        typeFilter.setOnAction(e -> filterTransactions());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("🔍 Search by category or description...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
        searchField.textProperty().addListener((obs, old, newVal) -> filterTransactions());

        filterBar.getChildren().addAll(filterLabel, periodFilter, typeFilter, spacer, searchField);
        return filterBar;
    }

    private void setupTableView() {
        tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        tableView.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    deleteTransaction(row.getItem());
                }
            });
            return row;
        });

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("#");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);
        typeCol.setCellFactory(col -> new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else {
                    setText(item.equals("INCOME") ? "💰 Income" : "💸 Expense");
                    setStyle(item.equals("INCOME") ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                            : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(120);
        amountCol.setCellFactory(col -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null)
                    setText(null);
                else {
                    Transaction t = getTableView().getItems().get(getIndex());
                    String prefix = t.getType().equals("INCOME") ? "+ $" : "- $";
                    setText(prefix + String.format("%.2f", amount));
                    setStyle(t.getType().equals("INCOME") ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                            : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(130);

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setPrefWidth(200);

        TableColumn<Transaction, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(120);
        dateCol.setCellFactory(col -> new TableCell<Transaction, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null)
                    setText(null);
                else
                    setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

        TableColumn<Transaction, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(80);
        actionCol.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button deleteBtn = new Button("🗑️");
            {
                deleteBtn.setStyle(
                        "-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-cursor: hand; -fx-font-size: 16px;");
                deleteBtn.setOnAction(e -> {
                    Transaction t = getTableView().getItems().get(getIndex());
                    deleteTransaction(t);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        tableView.getColumns().addAll(idCol, typeCol, amountCol, categoryCol, descriptionCol, dateCol, actionCol);
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionController.loadAllTransactions(currentUser.getId());

        String period = periodFilter.getValue();
        LocalDate now = LocalDate.now();
        LocalDate finalStartDate = transactionController.resolvePeriodStart(period, now);

        List<Transaction> filtered = transactionController.filterByPeriod(transactions, finalStartDate);

        transactionList = FXCollections.observableArrayList(filtered);
        filterTransactions();
    }

    private void filterTransactions() {
        ObservableList<Transaction> filtered = FXCollections.observableArrayList(
                transactionController.applySearchAndType(transactionList, typeFilter.getValue(), searchField.getText()));

        tableView.setItems(filtered);
        updateSummary(filtered);
    }

    private void updateSummary(ObservableList<Transaction> transactions) {
        TransactionController.Summary s = transactionController.summarize(transactions);
        totalIncomeLabel.setText(String.format("$%.2f", s.totalIncome()));
        totalExpenseLabel.setText(String.format("$%.2f", s.totalExpense()));
        netLabel.setText(String.format("$%.2f", s.net()));
        netLabel.setStyle(s.net() >= 0 ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    private HBox createSummaryBar() {
        HBox summaryBox = new HBox(20);
        summaryBox.setPadding(new Insets(15));
        summaryBox.getStyleClass().add("app-panel");
        summaryBox.setAlignment(Pos.CENTER);

        VBox incomeBox = new VBox(5);
        incomeBox.setAlignment(Pos.CENTER);
        Label incomeTitle = new Label("💰 Total Income");
        incomeTitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        totalIncomeLabel = new Label("$0.00");
        totalIncomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        incomeBox.getChildren().addAll(incomeTitle, totalIncomeLabel);

        VBox expenseBox = new VBox(5);
        expenseBox.setAlignment(Pos.CENTER);
        Label expenseTitle = new Label("💸 Total Expense");
        expenseTitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        totalExpenseLabel = new Label("$0.00");
        totalExpenseLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        expenseBox.getChildren().addAll(expenseTitle, totalExpenseLabel);

        VBox netBox = new VBox(5);
        netBox.setAlignment(Pos.CENTER);
        Label netTitle = new Label("💎 Net");
        netTitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        netLabel = new Label("$0.00");
        netLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f39c12;");
        netBox.getChildren().addAll(netTitle, netLabel);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        summaryBox.getChildren().addAll(incomeBox, spacer1, expenseBox, spacer2, netBox);
        return summaryBox;
    }

    private HBox createActionBar() {
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_RIGHT);

        Button addBtn = new Button("➕ Add Transaction");
        addBtn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        addBtn.setOnAction(e -> AddTransactionDialog.show(currentUser, () -> {
            loadTransactions();
            MainApp.refreshAfterDataChange();
        }));

        Button exportBtn = new Button("📥 Export CSV");
        exportBtn.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        exportBtn.setOnAction(e -> exportToCSV());

        actionBar.getChildren().addAll(addBtn, exportBtn);
        return actionBar;
    }

    private void deleteTransaction(Transaction t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this transaction?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                transactionController.deleteTransaction(t.getId(), currentUser.getId());
                loadTransactions();
                MainApp.refreshAfterDataChange();
                showAlert("Success", "Transaction deleted successfully!");
            }
        });
    }

    private void exportToCSV() {
        try {
            transactionController.exportToCsv(new java.util.ArrayList<>(tableView.getItems()), "transactions_export.csv");
            showAlert("Success", "Exported to transactions_export.csv in project folder!");
        } catch (Exception e) {
            showAlert("Error", "Export failed: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}