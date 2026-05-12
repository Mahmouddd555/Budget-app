package com.budget.ui;


import com.budget.models.User;
import com.budget.service.TransactionService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class SettingsView {
    private VBox view;
    private User currentUser;
    private SimpleBooleanProperty darkModeProperty;
    private static String selectedCurrency = "$";
    private static String selectedLanguage = "EN";

    public SettingsView(User user) {
        this.currentUser = user;
        this.darkModeProperty = new SimpleBooleanProperty(false);
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #f4f6f9;");

        if (currentUser == null)
            return;

        // Header
        Label title = new Label("⚙️ Settings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #2c3e50;");

        // Settings Grid
        GridPane settingsGrid = new GridPane();
        settingsGrid.setHgap(20);
        settingsGrid.setVgap(20);
        settingsGrid.setPadding(new Insets(20));
        settingsGrid.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        int row = 0;

        // ========== 1. DARK MODE ==========
        Label darkModeLabel = new Label("🌙 Dark Mode");
        darkModeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ToggleButton darkModeToggle = new ToggleButton();
        darkModeToggle.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");
        darkModeToggle.setText("OFF");
        darkModeToggle.setPrefWidth(80);

        darkModeToggle.selectedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                darkModeToggle.setText("ON");
                darkModeToggle
                        .setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-background-radius: 20;");
                applyDarkMode(true);
            } else {
                darkModeToggle.setText("OFF");
                darkModeToggle
                        .setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");
                applyDarkMode(false);
            }
        });

        settingsGrid.add(darkModeLabel, 0, row);
        settingsGrid.add(darkModeToggle, 1, row);
        row++;

        // ========== 2. CURRENCY ==========
        Label currencyLabel = new Label("💱 Currency");
        currencyLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD ($)", "EUR (€)", "EGP (£)", "GBP (£)", "JPY (¥)", "CAD ($)", "AUD ($)");
        currencyBox.setValue("USD ($)");
        currencyBox.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
        currencyBox.setOnAction(e -> {
            String selected = currencyBox.getValue();
            if (selected.contains("$"))
                selectedCurrency = "$";
            else if (selected.contains("€"))
                selectedCurrency = "€";
            else if (selected.contains("£"))
                selectedCurrency = "£";
            else if (selected.contains("¥"))
                selectedCurrency = "¥";
            showAlert("Currency Changed", "Currency set to " + selected);
        });

        settingsGrid.add(currencyLabel, 0, row);
        settingsGrid.add(currencyBox, 1, row);
        row++;

        // ========== 3. LANGUAGE ==========
        Label languageLabel = new Label("🌐 Language");
        languageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<String> languageBox = new ComboBox<>();
        languageBox.getItems().addAll("English (EN)", "العربية (AR)");
        languageBox.setValue("English (EN)");
        languageBox.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
        languageBox.setOnAction(e -> {
            String selected = languageBox.getValue();
            if (selected.contains("EN"))
                selectedLanguage = "EN";
            else
                selectedLanguage = "AR";
            showAlert("Language Changed", "Language set to " + selected + ". Please restart the app.");
        });

        settingsGrid.add(languageLabel, 0, row);
        settingsGrid.add(languageBox, 1, row);
        row++;

        // ========== 4. DEFAULT VIEW ==========
        Label defaultViewLabel = new Label("🏠 Default View");
        defaultViewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<String> defaultViewBox = new ComboBox<>();
        defaultViewBox.getItems().addAll("Dashboard", "Transactions", "Budgets", "Goals");
        defaultViewBox.setValue("Dashboard");
        defaultViewBox.setStyle("-fx-padding: 8; -fx-background-radius: 8;");

        settingsGrid.add(defaultViewLabel, 0, row);
        settingsGrid.add(defaultViewBox, 1, row);
        row++;

        // ========== 5. BUDGET ALERT THRESHOLD ==========
        Label thresholdLabel = new Label("⚠️ Budget Alert Threshold");
        thresholdLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Slider thresholdSlider = new Slider(50, 100, 80);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setShowTickMarks(true);
        thresholdSlider.setMajorTickUnit(10);
        thresholdSlider.setBlockIncrement(5);
        thresholdSlider.setPrefWidth(200);

        Label thresholdValue = new Label("80%");
        thresholdValue.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        thresholdSlider.valueProperty().addListener((obs, old, newVal) -> {
            thresholdValue.setText(String.format("%.0f%%", newVal));
        });

        HBox thresholdBox = new HBox(10, thresholdSlider, thresholdValue);
        thresholdBox.setAlignment(Pos.CENTER_LEFT);

        settingsGrid.add(thresholdLabel, 0, row);
        settingsGrid.add(thresholdBox, 1, row);
        row++;

        // ========== 6. NOTIFICATION SETTINGS ==========
        Label notificationLabel = new Label("🔔 Daily Notifications");
        notificationLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ToggleButton notificationToggle = new ToggleButton();
        notificationToggle.setText("OFF");
        notificationToggle.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20;");
        notificationToggle.setPrefWidth(80);
        notificationToggle.selectedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                notificationToggle.setText("ON");
                notificationToggle
                        .setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 20;");
                showAlert("Notifications", "Daily notifications enabled! You'll get reminders at 9:00 AM.");
            } else {
                notificationToggle.setText("OFF");
                notificationToggle
                        .setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20;");
                showAlert("Notifications", "Daily notifications disabled.");
            }
        });

        settingsGrid.add(notificationLabel, 0, row);
        settingsGrid.add(notificationToggle, 1, row);
        row++;

        // ========== 7. AUTO BACKUP ==========
        Label autoBackupLabel = new Label("💾 Auto Backup (Weekly)");
        autoBackupLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ToggleButton autoBackupToggle = new ToggleButton();
        autoBackupToggle.setText("OFF");
        autoBackupToggle.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20;");
        autoBackupToggle.setPrefWidth(80);
        autoBackupToggle.selectedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                autoBackupToggle.setText("ON");
                autoBackupToggle
                        .setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 20;");
                showAlert("Auto Backup", "Weekly automatic backup enabled.");
            } else {
                autoBackupToggle.setText("OFF");
                autoBackupToggle
                        .setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20;");
                showAlert("Auto Backup", "Automatic backup disabled.");
            }
        });

        settingsGrid.add(autoBackupLabel, 0, row);
        settingsGrid.add(autoBackupToggle, 1, row);
        row++;

        // ========== 8. FONT SIZE ==========
        Label fontSizeLabel = new Label("🔤 Font Size");
        fontSizeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<String> fontSizeBox = new ComboBox<>();
        fontSizeBox.getItems().addAll("Small (12px)", "Medium (14px)", "Large (16px)", "Extra Large (18px)");
        fontSizeBox.setValue("Medium (14px)");
        fontSizeBox.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
        fontSizeBox.setOnAction(e -> {
            String selected = fontSizeBox.getValue();
            int size = 14;
            if (selected.contains("Small"))
                size = 12;
            else if (selected.contains("Medium"))
                size = 14;
            else if (selected.contains("Large"))
                size = 16;
            else if (selected.contains("Extra"))
                size = 18;
            applyFontSize(size);
        });

        settingsGrid.add(fontSizeLabel, 0, row);
        settingsGrid.add(fontSizeBox, 1, row);
        row++;

        // ========== 9. CHANGE PASSWORD ==========
        Label passwordLabel = new Label("🔑 Change Password");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog());

        settingsGrid.add(passwordLabel, 0, row);
        settingsGrid.add(changePasswordBtn, 1, row);
        row++;

        // ========== 10. BACKUP & RESTORE ==========
        Label backupLabel = new Label("💾 Manual Backup & Restore");
        backupLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox backupBox = new HBox(10);
        Button backupBtn = new Button("📥 Backup Database");
        backupBtn.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        backupBtn.setOnAction(e -> backupDatabase());

        Button restoreBtn = new Button("📤 Restore Database");
        restoreBtn.setStyle(
                "-fx-background-color: #f39c12; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        restoreBtn.setOnAction(e -> restoreDatabase());

        backupBox.getChildren().addAll(backupBtn, restoreBtn);
        settingsGrid.add(backupLabel, 0, row);
        settingsGrid.add(backupBox, 1, row);
        row++;

        // ========== 11. EXPORT REPORTS ==========
        Label exportLabel = new Label("📊 Export Reports");
        exportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox exportBox = new HBox(10);
        Button exportCSVBtn = new Button("📄 Export as CSV");
        exportCSVBtn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        exportCSVBtn.setOnAction(e -> exportToCSV());

        Button exportPDFBtn = new Button("📑 Export as PDF");
        exportPDFBtn.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        exportPDFBtn.setDisable(true);
        exportPDFBtn.setText("📑 PDF (Coming Soon)");

        exportBox.getChildren().addAll(exportCSVBtn, exportPDFBtn);
        settingsGrid.add(exportLabel, 0, row);
        settingsGrid.add(exportBox, 1, row);
        row++;

        // ========== 12. CLEAR DATA ==========
        Label clearLabel = new Label("⚠️ Danger Zone");
        clearLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Button clearDataBtn = new Button("🗑️ Clear All Data");
        clearDataBtn.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 8; -fx-cursor: hand;");
        clearDataBtn.setOnAction(e -> confirmClearData());

        settingsGrid.add(clearLabel, 0, row);
        settingsGrid.add(clearDataBtn, 1, row);
        row++;

        // ========== 13. ABOUT ==========
        Label aboutLabel = new Label("ℹ️ About");
        aboutLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox aboutBox = new VBox(5);
        Label versionLabel = new Label("Budget Manager Pro v2.0.0");
        versionLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
        Label authorLabel = new Label("Developed by Mazen");
        authorLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        Label dateLabel = new Label("© 2026 - All Rights Reserved");
        dateLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");
        aboutBox.getChildren().addAll(versionLabel, authorLabel, dateLabel);

        settingsGrid.add(aboutLabel, 0, row);
        settingsGrid.add(aboutBox, 1, row);

        view.getChildren().addAll(title, settingsGrid);
    }

    private void applyDarkMode(boolean dark) {
        ThemeManager.setDarkMode(dark);
        ThemeManager.refreshCurrentView();

        // تحديث نص الزر
        if (dark) {
            showAlert("Dark Mode", "Dark mode enabled on all screens!");
        } else {
            showAlert("Light Mode", "Light mode enabled on all screens!");
        }
    }

    private void applyFontSize(int size) {
        String fontSize = size + "px";
        view.setStyle("-fx-font-size: " + fontSize + ";");
        showAlert("Font Size", "Font size changed to " + size + "px");
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Change your password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Old Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        grid.add(new Label("Old Password:"), 0, 0);
        grid.add(oldPasswordField, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("Confirm:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                    showAlert("Success", "Password changed successfully!");
                } else {
                    showAlert("Error", "New passwords do not match!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void backupDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup");
        fileChooser.setInitialFileName("budget_backup_" + System.currentTimeMillis() + ".db");
        File destFile = fileChooser.showSaveDialog(null);

        if (destFile != null) {
            try {
                File sourceFile = new File("budget_manager.db");
                if (sourceFile.exists()) {
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert("Success", "Database backed up successfully!");
                } else {
                    showAlert("Error", "No database file found to backup!");
                }
            } catch (Exception e) {
                showAlert("Error", "Backup failed: " + e.getMessage());
            }
        }
    }

    private void restoreDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Restore Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db"));
        File sourceFile = fileChooser.showOpenDialog(null);

        if (sourceFile != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Restore");
            confirm.setHeaderText("⚠️ Warning!");
            confirm.setContentText("Restoring will overwrite current data. Continue?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        File destFile = new File("budget_manager.db");
                        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        showAlert("Success", "Database restored. Please restart the app.");
                    } catch (Exception e) {
                        showAlert("Error", "Restore failed: " + e.getMessage());
                    }
                }
            });
        }
    }

    private void exportToCSV() {
        try {
            String filename = "transactions_export_" + System.currentTimeMillis() + ".csv";
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.append("ID,Type,Amount,Category,Description,Date\n");

            TransactionService service = new TransactionService();
            var transactions = service.getUserTransactions(currentUser.getId());

            for (var t : transactions) {
                writer.append(t.getId() + ",");
                writer.append(t.getType() + ",");
                writer.append(t.getAmount() + ",");
                writer.append(t.getCategory() + ",");
                writer.append(t.getDescription() + ",");
                writer.append(t.getDate().toString() + "\n");
            }

            writer.flush();
            writer.close();
            showAlert("Success", "Exported to " + filename);
        } catch (Exception e) {
            showAlert("Error", "Export failed: " + e.getMessage());
        }
    }

    private void confirmClearData() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear All Data");
        confirm.setHeaderText("⚠️ DANGER ZONE ⚠️");
        confirm.setContentText("This will delete ALL your data permanently!\n\nAre you absolutely sure?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showAlert("Cleared", "All data cleared. Restart the app.");
            }
        });
    }

    public static String getCurrencySymbol() {
        return selectedCurrency;
    }

    public static String getLanguage() {
        return selectedLanguage;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}