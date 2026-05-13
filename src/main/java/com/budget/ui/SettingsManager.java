package com.budget.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import java.util.prefs.Preferences;

public class SettingsManager {
    private static Preferences prefs = Preferences.userNodeForPackage(SettingsManager.class);
    private static Scene currentScene;

    // الإعدادات
    private static String currency = "$";
    private static String language = "EN";
    private static double fontSize = 14;
    private static String defaultView = "Dashboard";
    private static int budgetAlertThreshold = 80;
    private static boolean notificationsEnabled = false;
    private static boolean autoBackupEnabled = false;

    public static void init(Scene scene) {
        currentScene = scene;
        loadAllSettings();
        applyAllSettings();
    }

    private static void loadAllSettings() {
        currency = prefs.get("CURRENCY", "$");
        language = prefs.get("LANGUAGE", "EN");
        fontSize = prefs.getDouble("FONT_SIZE", 14);
        defaultView = prefs.get("DEFAULT_VIEW", "Dashboard");
        budgetAlertThreshold = prefs.getInt("BUDGET_ALERT_THRESHOLD", 80);
        notificationsEnabled = prefs.getBoolean("NOTIFICATIONS_ENABLED", false);
        autoBackupEnabled = prefs.getBoolean("AUTO_BACKUP_ENABLED", false);
    }

    public static void applyAllSettings() {
        applyFontSize();
        // اللغه والعملة هتطبق عند عرض الأرقام
    }

    public static void applyFontSize() {
        if (currentScene != null) {
            String fontStyle = String.format("-fx-font-size: %.0fpx;", fontSize);
            currentScene.getRoot().setStyle(fontStyle);
        }
    }

    // ========== Currency ==========
    public static String getCurrency() {
        return currency;
    }

    public static void setCurrency(String curr) {
        currency = curr;
        prefs.put("CURRENCY", curr);
        refreshAllViews();
    }

    // ========== Language ==========
    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String lang) {
        language = lang;
        prefs.put("LANGUAGE", lang);
        refreshAllViews();
    }

    // ========== Font Size ==========
    public static double getFontSize() {
        return fontSize;
    }

    public static void setFontSize(double size) {
        fontSize = size;
        prefs.putDouble("FONT_SIZE", size);
        applyFontSize();
    }

    // ========== Default View ==========
    public static String getDefaultView() {
        return defaultView;
    }

    public static void setDefaultView(String view) {
        defaultView = view;
        prefs.put("DEFAULT_VIEW", view);
    }

    // ========== Budget Alert Threshold ==========
    public static int getBudgetAlertThreshold() {
        return budgetAlertThreshold;
    }

    public static void setBudgetAlertThreshold(int threshold) {
        budgetAlertThreshold = threshold;
        prefs.putInt("BUDGET_ALERT_THRESHOLD", threshold);
    }

    // ========== Notifications ==========
    public static boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public static void setNotificationsEnabled(boolean enabled) {
        notificationsEnabled = enabled;
        prefs.putBoolean("NOTIFICATIONS_ENABLED", enabled);
    }

    // ========== Auto Backup ==========
    public static boolean isAutoBackupEnabled() {
        return autoBackupEnabled;
    }

    public static void setAutoBackupEnabled(boolean enabled) {
        autoBackupEnabled = enabled;
        prefs.putBoolean("AUTO_BACKUP_ENABLED", enabled);
    }

    // ========== Refresh ==========
    private static void refreshAllViews() {
        if (currentScene != null) {
            currentScene.getRoot().setStyle(String.format("-fx-font-size: %.0fpx;", fontSize));
        }
    }

    // ========== Format Money ==========
    public static String formatMoney(double amount) {
        String symbol = currency;
        return String.format("%s%.2f", symbol, amount);
    }
}