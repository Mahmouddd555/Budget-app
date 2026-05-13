package com.budget.ui;

import com.budget.models.User;
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

/**
 * Top navigation layout; balance text is updated via {@link #updateBalance(double)} after data changes.
 */
public class TopNavBar {
    private final HBox view;
    private final User currentUser;
    private final Label titleLabel;
    private final Label dateLabel;
    private Label balanceLabel;

    public TopNavBar(User user) {
        this.currentUser = user;
        view = new HBox(15);
        view.setPadding(new Insets(15, 25, 15, 25));
        view.setAlignment(Pos.CENTER_LEFT);
        view.getStyleClass().add("top-navbar");

        titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.getStyleClass().add("top-navbar-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        dateLabel = new Label("📅 " + today);
        dateLabel.getStyleClass().add("top-navbar-date");

        if (currentUser != null) {
            balanceLabel = new Label(formatBalance(currentUser.getTotalBalance()));
            balanceLabel.getStyleClass().add("top-navbar-balance");

            Label userLabel = new Label("👤 " + currentUser.getUsername());
            userLabel.getStyleClass().add("top-navbar-user");

            view.getChildren().addAll(titleLabel, spacer, dateLabel, balanceLabel, userLabel);
        } else {
            view.getChildren().addAll(titleLabel, spacer, dateLabel);
        }
    }

    private static String formatBalance(double balance) {
        return "💰 Balance: " + SettingsManager.formatMoney(balance);
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
            balanceLabel.setText(formatBalance(newBalance));
        }
    }

    public HBox getView() {
        return view;
    }
}
