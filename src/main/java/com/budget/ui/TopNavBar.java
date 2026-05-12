package com.budget.ui;

import com.budget.models.User;
import com.budget.service.TransactionService;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TopNavBar {
    private HBox view;
    private User currentUser;
    private Label titleLabel;
    private Label dateLabel;
    private Label balanceLabel;

    public TopNavBar(User user) {
        this.currentUser = user;
        initializeView();
    }

    private void initializeView() {
        view = new HBox(15);
        view.setPadding(new Insets(15, 25, 15, 25));
        view.setAlignment(Pos.CENTER_LEFT);
        view.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 5, 0, 0, 1);");

        titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Current date
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        dateLabel = new Label("📅 " + today);
        dateLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");

        // Balance if logged in
        if (currentUser != null) {
            double balance = currentUser.getTotalBalance();
            balanceLabel = new Label(String.format("💰 Balance: $%.2f", balance));
            balanceLabel.setStyle(
                    "-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 0 15 0 15;");

            Label userLabel = new Label("👤 " + currentUser.getUsername());
            userLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold; -fx-font-size: 14px;");

            view.getChildren().addAll(titleLabel, spacer, dateLabel, balanceLabel, userLabel);
        } else {
            view.getChildren().addAll(titleLabel, spacer, dateLabel);
        }
    }

    public void updateTitle(String title) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), titleLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        titleLabel.setText(title);
        ft.play();
    }

    public void updateBalance(double newBalance) {
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("💰 Balance: $%.2f", newBalance));
        }
    }

    public HBox getView() {
        return view;
    }
}