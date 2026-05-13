package com.budget.ui;

import com.budget.util.CssResources;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Switches between centralized light and dark stylesheets on the main scene.
 * Styles live under {@code /css/light-theme.css} and {@code /css/dark-theme.css}.
 */
public class ThemeManager {
    private static boolean isDarkMode = false;
    private static Scene currentScene;
    private static BorderPane rootLayout;

    public static void init(Scene scene, BorderPane root) {
        currentScene = scene;
        rootLayout = root;
        applyTheme();
    }

    public static void setDarkMode(boolean dark) {
        isDarkMode = dark;
        applyTheme();
    }

    public static boolean isDarkMode() {
        return isDarkMode;
    }

    private static void applyTheme() {
        if (currentScene == null) {
            return;
        }

        String light = CssResources.lightThemeExternalForm();
        String dark = CssResources.darkThemeExternalForm();

        currentScene.getStylesheets().clear();

        if (isDarkMode) {
            currentScene.setFill(Color.web("#121212"));
            if (rootLayout != null) {
                rootLayout.setStyle("-fx-background-color: #121212;");
            }
            if (dark != null) {
                currentScene.getStylesheets().add(dark);
            }
        } else {
            currentScene.setFill(Color.web("#f4f6f9"));
            if (rootLayout != null) {
                rootLayout.setStyle("-fx-background-color: #f4f6f9;");
            }
            if (light != null) {
                currentScene.getStylesheets().add(light);
            }
        }
    }

    public static void refreshCurrentView() {
        if (rootLayout != null) {
            applyTheme();
        }
    }
}
