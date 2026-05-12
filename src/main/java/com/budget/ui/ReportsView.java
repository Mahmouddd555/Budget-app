package com.budget.ui;

import com.budget.models.User;
import com.budget.service.TransactionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class ReportsView {
    private VBox view;
    private User currentUser;
    private TransactionService transactionService;

    public ReportsView(User user) {
        this.currentUser = user;
        this.transactionService = new TransactionService();
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #f4f6f9;");

        if (currentUser == null)
            return;

        Label title = new Label("📊 Reports");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Label comingSoon = new Label("Reports feature coming soon!");
        comingSoon.setFont(Font.font("Segoe UI", 18));
        comingSoon.setStyle("-fx-text-fill: #7f8c8d;");

        VBox centerBox = new VBox(comingSoon);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(100, 0, 0, 0));

        view.getChildren().addAll(title, centerBox);
    }

    public VBox getView() {
        return view;
    }
}