package com.budget.ui;

import com.budget.controller.TransactionController;
import com.budget.models.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;

/**
 * Modal form for adding a transaction; delegates persistence to {@link TransactionController}.
 */
public class AddTransactionDialog {

    public static void show(User currentUser, Runnable onSuccess) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText(null);

        TransactionController controller = new TransactionController();

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("app-panel");

        Label title = new Label("New Transaction");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.getStyleClass().add("app-section-title");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        Label typeLabel = new Label("Type:");
        typeLabel.getStyleClass().add("app-heading");
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton incomeRadio = new RadioButton("Income");
        RadioButton expenseRadio = new RadioButton("Expense");
        incomeRadio.setToggleGroup(typeGroup);
        expenseRadio.setToggleGroup(typeGroup);
        expenseRadio.setSelected(true);
        HBox typeBox = new HBox(15, incomeRadio, expenseRadio);

        Label amountLabel = new Label("Amount:");
        amountLabel.getStyleClass().add("app-heading");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        Label categoryLabel = new Label("Category:");
        categoryLabel.getStyleClass().add("app-heading");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Transport", "Shopping", "Entertainment",
                "Bills", "Healthcare", "Education", "Salary",
                "Freelance", "Investment", "Other");
        categoryBox.setValue("Food");

        Label descLabel = new Label("Description:");
        descLabel.getStyleClass().add("app-heading");
        TextField descField = new TextField();
        descField.setPromptText("Optional");

        Label dateLabel = new Label("Date:");
        dateLabel.getStyleClass().add("app-heading");
        DatePicker datePicker = new DatePicker(LocalDate.now());

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

        incomeRadio.selectedProperty().addListener((obs, old, newVal) -> {
            if (Boolean.TRUE.equals(newVal)) {
                categoryBox.getItems().setAll("Salary", "Freelance", "Investment", "Gift", "Other");
                categoryBox.setValue("Salary");
            }
        });
        expenseRadio.selectedProperty().addListener((obs, old, newVal) -> {
            if (Boolean.TRUE.equals(newVal)) {
                categoryBox.getItems().setAll("Food", "Transport", "Shopping", "Entertainment",
                        "Bills", "Healthcare", "Education", "Other");
                categoryBox.setValue("Food");
            }
        });

        content.getChildren().addAll(title, grid);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("btn-primary");
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("btn-danger");

        dialog.setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) {
                return null;
            }
            try {
                String type = incomeRadio.isSelected() ? "INCOME" : "EXPENSE";
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryBox.getValue();
                String description = descField.getText().isEmpty() ? "-" : descField.getText();
                LocalDate date = datePicker.getValue();

                if (amount <= 0) {
                    Platform.runLater(() -> showAlert("Error", "Amount must be greater than 0!"));
                    return null;
                }
                if (date == null) {
                    Platform.runLater(() -> showAlert("Error", "Please select a date."));
                    return null;
                }

                boolean success = controller.addTransaction(currentUser.getId(), type, amount, category, description,
                        date);
                if (success) {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                    MainApp.refreshAfterDataChange();
                    Platform.runLater(() -> showAlert("Success", "Transaction added successfully!"));
                } else {
                    Platform.runLater(() -> showAlert("Error", "Could not save transaction."));
                }
            } catch (NumberFormatException e) {
                Platform.runLater(() -> showAlert("Error", "Please enter a valid amount!"));
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
