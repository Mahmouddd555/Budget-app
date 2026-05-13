package com.budget.ui;

import com.budget.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReportsView {
    private VBox view;
    private User currentUser;

    public ReportsView(User user) {
        this.currentUser = user;
        initializeView();
    }

    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("app-page");

        if (currentUser == null) {
            return;
        }

        Label title = new Label("📊 Reports");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.getStyleClass().add("app-heading");

        Label comingSoon = new Label("Reports feature coming soon!");
        comingSoon.setFont(Font.font("Segoe UI", 18));
        comingSoon.getStyleClass().add("app-muted");

        VBox centerBox = new VBox(comingSoon);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(100, 0, 0, 0));

        view.getChildren().addAll(title, centerBox);
    }

    public VBox getView() {
        return view;
    }
}
