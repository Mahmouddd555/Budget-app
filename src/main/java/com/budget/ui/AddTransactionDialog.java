package com.budget.ui;

import com.budget.models.User;
import com.budget.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.time.LocalDate;

public class AddTransactionDialog {

    public static void show(User currentUser, Runnable onSuccess) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText(null);

        // Custom dialog content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white;");

        Text title = new Text("➕ New Transaction");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setFill(Color.web("#2c3e50"));

        // Form fields
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Type selection
        Label typeLabel = new Label("Type:");
        typeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton incomeRadio = new RadioButton("💰 Income");
        RadioButton expenseRadio = new RadioButton("💸 Expense");
        incomeRadio.setToggleGroup(typeGroup);
        expenseRadio.setToggleGroup(typeGroup);
        expenseRadio.setSelected(true);
        HBox typeBox = new HBox(15, incomeRadio, expenseRadio);

        // Amount field
        Label amountLabel = new Label("Amount:");
        amountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.setStyle("-fx-padding: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");

        // Category
        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Transport", "Shopping", "Entertainment",
                "Bills", "Healthcare", "Education", "Salary",
                "Freelance", "Investment", "Other");
        categoryBox.setValue("Food");
        categoryBox.setStyle("-fx-padding: 5;");

        // Description
        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField descField = new TextField();
        descField.setPromptText("Optional");
        descField.setStyle("-fx-padding: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");

        // Date
        Label dateLabel = new Label("Date:");
        dateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-padding: 5;");

        grid.add(typeLabel, 0, 0);
        grid.add(typeBox, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(categoryLabel, 0, 2);
        grid.add(categoryBox, 1, 2);
        grid.add(descLabel, 0, 3);
        grid.add(descField, 1, 3);
        grid.add(dateLabel, 0, 4);
        grid.add(datePicker, 1, 4);

        // Update categories based on type
        incomeRadio.selectedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                categoryBox.getItems().setAll("Salary", "Freelance", "Investment", "Gift", "Other");
                categoryBox.setValue("Salary");
            }
        });
        expenseRadio.selectedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                categoryBox.getItems().setAll("Food", "Transport", "Shopping", "Entertainment",
                        "Bills", "Healthcare", "Education", "Other");
                categoryBox.setValue("Food");
            }
        });

        content.getChildren().addAll(title, grid);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Style buttons
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;");
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle(
                "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String type = incomeRadio.isSelected() ? "INCOME" : "EXPENSE";
                    double amount = Double.parseDouble(amountField.getText());
                    String category = categoryBox.getValue();
                    String description = descField.getText().isEmpty() ? "-" : descField.getText();
                    LocalDate date = datePicker.getValue();

                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than 0!");
                        return null;
                    }

                    TransactionService transactionService = new TransactionService();
                    boolean success = transactionService.addTransaction(currentUser.getId(), type, amount, category,
                            description, date);

                    if (success && onSuccess != null) {
                        onSuccess.run();
                        MainApp.refreshDashboard();
                        showAlert("Success", "Transaction added successfully!");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid amount!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}