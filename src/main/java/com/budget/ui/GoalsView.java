package com.budget.ui;

import com.budget.models.User;
import com.budget.models.Goal;
import com.budget.service.GoalService;
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

public class GoalsView {
    private VBox view;
    private User currentUser;
    private GoalService goalService;
    private TableView<Goal> tableView;
    private ObservableList<Goal> goalList;
    private TabPane tabPane;

    public GoalsView(User user) {
        this.currentUser = user;
        this.goalService = new GoalService();
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #f4f6f9;");

        if (currentUser == null)
            return;

        // Header
        HBox header = createHeader();

        // Summary Cards
        GridPane summaryCards = createSummaryCards();

        // Tab Pane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");

        Tab activeTab = new Tab("🎯 Active Goals");
        Tab completedTab = new Tab("✅ Completed");

        loadGoals();
        activeTab.setContent(createGoalsTable("ACTIVE"));
        completedTab.setContent(createGoalsTable("ACHIEVED"));

        tabPane.getTabs().addAll(activeTab, completedTab);

        // Add Button
        HBox actionBar = createActionBar();

        view.getChildren().addAll(header, summaryCards, tabPane, actionBar);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("🏆 Financial Goals");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tipLabel = new Label("💡 Set goals and track your progress!");
        tipLabel.setStyle(
                "-fx-background-color: #ecf0f1; -fx-padding: 8 15; -fx-background-radius: 20; -fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(title, spacer, tipLabel);
        return header;
    }

    private GridPane createSummaryCards() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 0, 20, 0));

        loadGoals();

        double totalTarget = goalList.stream().mapToDouble(Goal::getTargetAmount).sum();
        double totalSaved = goalList.stream().mapToDouble(Goal::getSavedAmount).sum();
        int activeCount = (int) goalList.stream().filter(g -> g.getStatus().equals("ACTIVE")).count();

        VBox targetCard = createStatCard("🎯 Total Goals", String.format("$%.2f", totalTarget), "#3498db");
        VBox savedCard = createStatCard("💰 Total Saved", String.format("$%.2f", totalSaved), "#27ae60");
        VBox activeCard = createStatCard("📊 Active Goals", String.valueOf(activeCount), "#f39c12");

        grid.add(targetCard, 0, 0);
        grid.add(savedCard, 1, 0);
        grid.add(activeCard, 2, 0);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            grid.getColumnConstraints().add(col);
        }

        return grid;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: %s;", color));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void loadGoals() {
        goalList = FXCollections.observableArrayList(goalService.getUserGoals(currentUser.getId()));
    }

    private TableView<Goal> createGoalsTable(String status) {
        TableView<Goal> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<Goal, Integer> idCol = new TableColumn<>("#");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Goal, String> nameCol = new TableColumn<>("Goal");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<Goal, Double> targetCol = new TableColumn<>("Target");
        targetCol.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));
        targetCol.setPrefWidth(120);
        targetCol.setCellFactory(col -> new TableCell<Goal, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : String.format("$%.2f", amount));
            }
        });

        TableColumn<Goal, Double> savedCol = new TableColumn<>("Saved");
        savedCol.setCellValueFactory(new PropertyValueFactory<>("savedAmount"));
        savedCol.setPrefWidth(120);
        savedCol.setCellFactory(col -> new TableCell<Goal, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : String.format("$%.2f", amount));
            }
        });

        TableColumn<Goal, Double> progressCol = new TableColumn<>("Progress");
        progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressCol.setPrefWidth(180);
        progressCol.setCellFactory(col -> new TableCell<Goal, Double>() {
            private ProgressBar progressBar = new ProgressBar();

            @Override
            protected void updateItem(Double progress, boolean empty) {
                super.updateItem(progress, empty);
                if (empty || progress == null)
                    setGraphic(null);
                else {
                    progressBar.setProgress(progress / 100.0);
                    progressBar.setPrefWidth(100);
                    HBox container = new HBox(10, progressBar, new Label(String.format("%.1f%%", progress)));
                    container.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(container);
                }
            }
        });

        TableColumn<Goal, LocalDate> dateCol = new TableColumn<>("Target Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("targetDate"));
        dateCol.setPrefWidth(120);
        dateCol.setCellFactory(col -> new TableCell<Goal, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

        TableColumn<Goal, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<Goal, Void>() {
            private final Button addBtn = new Button("➕ Add");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox container = new HBox(5);
            {
                addBtn.setStyle(
                        "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10; -fx-background-radius: 5;");
                deleteBtn.setStyle(
                        "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10; -fx-background-radius: 5;");
                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setGraphic(null);
                else {
                    Goal g = getTableView().getItems().get(getIndex());
                    container.getChildren().clear();
                    if (g.getStatus().equals("ACTIVE")) {
                        addBtn.setOnAction(e -> addFunds(g));
                        container.getChildren().add(addBtn);
                    }
                    deleteBtn.setOnAction(e -> deleteGoal(g));
                    container.getChildren().add(deleteBtn);
                    setGraphic(container);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, targetCol, savedCol, progressCol, dateCol, actionCol);

        ObservableList<Goal> filtered = FXCollections.observableArrayList(
                goalList.stream().filter(g -> g.getStatus().equals(status)).toList());
        table.setItems(filtered);

        return table;
    }

    private HBox createActionBar() {
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_RIGHT);

        Button addBtn = new Button("➕ Add New Goal");
        addBtn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        addBtn.setOnAction(e -> showCreateGoalDialog());

        actionBar.getChildren().add(addBtn);
        return actionBar;
    }

    private void showCreateGoalDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Goal");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Set Your Financial Goal");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        TextField nameField = new TextField();
        nameField.setPromptText("Goal Name (e.g., New Car, Vacation, Emergency Fund)");

        TextField targetField = new TextField();
        targetField.setPromptText("Target Amount");

        DatePicker datePicker = new DatePicker(LocalDate.now().plusMonths(6));

        content.getChildren().addAll(title, nameField, targetField, datePicker);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    double target = Double.parseDouble(targetField.getText());
                    LocalDate targetDate = datePicker.getValue();
                    if (!name.isEmpty() && target > 0) {
                        goalService.createGoal(currentUser.getId(), name, target, targetDate);
                        refreshView();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void addFunds(Goal goal) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Funds");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label info = new Label("Goal: " + goal.getName());
        info.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        Label progress = new Label(
                String.format("Current: $%.2f / $%.2f", goal.getSavedAmount(), goal.getTargetAmount()));

        TextField amountField = new TextField();
        amountField.setPromptText("Amount to add");

        content.getChildren().addAll(info, progress, amountField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    double newSaved = goal.getSavedAmount() + amount;
                    goalService.updateSavedAmount(goal.getId(), newSaved);
                    if (newSaved >= goal.getTargetAmount()) {
                        goalService.updateGoalStatus(goal.getId(), "ACHIEVED");
                    }
                    refreshView();
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteGoal(Goal goal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Goal");
        alert.setContentText("Delete goal: " + goal.getName() + "?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                goalService.deleteGoal(goal.getId());
                refreshView();
            }
        });
    }

    private void refreshView() {
        loadGoals();
        tabPane.getTabs().get(0).setContent(createGoalsTable("ACTIVE"));
        tabPane.getTabs().get(1).setContent(createGoalsTable("ACHIEVED"));
        GridPane newSummary = createSummaryCards();
        view.getChildren().set(1, newSummary);
    }

    public VBox getView() {
        return view;
    }
}