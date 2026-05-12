package com.budget.ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

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
        if (currentScene == null)
            return;

        if (isDarkMode) {
            // Dark Mode - يغير كل حرفيًا كل حاجة
            currentScene.setFill(Color.web("#121212"));
            rootLayout.setStyle("-fx-background-color: #121212;");

            String darkStyle = """
                    /* ====== RESET EVERYTHING ====== */
                    .root {
                        -fx-base: #1e1e1e;
                        -fx-background: #121212;
                        -fx-control-inner-background: #2d2d2d;
                        -fx-accent: #1f6feb;
                        -fx-default-button: #1f6feb;
                        -fx-focus-color: #1f6feb;
                        -fx-faint-focus-color: #1f6feb22;
                    }

                    /* ====== MAIN BACKGROUND ====== */
                    .root, .border-pane, .vbox, .hbox, .grid-pane, .anchor-pane {
                        -fx-background-color: #121212;
                    }

                    /* ====== SIDEBAR FULL DARK ====== */
                    .sidebar {
                        -fx-background-color: linear-gradient(to bottom, #0d1117, #0a0a0f);
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);
                    }

                    .sidebar-button {
                        -fx-background-color: transparent;
                        -fx-text-fill: #e6edf3;
                        -fx-font-size: 14px;
                        -fx-padding: 12 15 12 15;
                        -fx-cursor: hand;
                        -fx-alignment: CENTER_LEFT;
                    }

                    .sidebar-button:hover {
                        -fx-background-color: #2d3a4a;
                        -fx-text-fill: white;
                    }

                    .sidebar-button-active {
                        -fx-background-color: #1f6feb;
                        -fx-text-fill: white;
                        -fx-background-radius: 8;
                    }

                    /* ====== TOP NAVIGATION BAR FULL DARK ====== */
                    .top-navbar {
                        -fx-background-color: #161b22;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);
                    }

                    .top-navbar-title {
                        -fx-text-fill: #f0f6fc;
                        -fx-font-size: 22px;
                        -fx-font-weight: bold;
                    }

                    .top-navbar-user {
                        -fx-text-fill: #8b949e;
                    }

                    /* ====== CARDS FULL DARK ====== */
                    .card {
                        -fx-background-color: #21262d;
                        -fx-background-radius: 15;
                        -fx-padding: 20;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);
                    }

                    .card:hover {
                        -fx-background-color: #2d3a4a;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3);
                    }

                    .card-title {
                        -fx-text-fill: #8b949e;
                        -fx-font-size: 14px;
                    }

                    .card-value {
                        -fx-font-size: 28px;
                        -fx-font-weight: bold;
                        -fx-text-fill: #e6edf3;
                    }

                    .card-positive {
                        -fx-text-fill: #3fb950;
                    }

                    .card-negative {
                        -fx-text-fill: #f85149;
                    }

                    /* ====== ALL LABELS ====== */
                    .label {
                        -fx-text-fill: #e6edf3;
                    }

                    /* ====== TABLES FULL DARK ====== */
                    .table-view {
                        -fx-background-color: #21262d;
                        -fx-background-radius: 10;
                        -fx-border-color: #30363d;
                        -fx-border-radius: 10;
                    }

                    .table-view .column-header-background {
                        -fx-background-color: #161b22;
                        -fx-background-radius: 10 10 0 0;
                    }

                    .table-view .column-header {
                        -fx-background-color: transparent;
                        -fx-pref-height: 40;
                    }

                    .table-view .column-header .label {
                        -fx-text-fill: #e6edf3;
                        -fx-font-weight: bold;
                    }

                    .table-row-cell {
                        -fx-background-color: #21262d;
                        -fx-text-fill: #e6edf3;
                    }

                    .table-row-cell:odd {
                        -fx-background-color: #1a1f26;
                    }

                    .table-row-cell:hover {
                        -fx-background-color: #2d3a4a;
                    }

                    .table-cell {
                        -fx-text-fill: #e6edf3;
                    }

                    /* ====== BUTTONS ====== */
                    .button {
                        -fx-cursor: hand;
                        -fx-background-radius: 8;
                    }

                    .btn-primary {
                        -fx-background-color: #1f6feb;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-padding: 10 20;
                    }

                    .btn-primary:hover {
                        -fx-background-color: #388bfd;
                    }

                    .btn-danger {
                        -fx-background-color: #f85149;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-padding: 8 15;
                    }

                    .btn-danger:hover {
                        -fx-background-color: #da3633;
                    }

                    .btn-success {
                        -fx-background-color: #238636;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-padding: 8 15;
                    }

                    .btn-success:hover {
                        -fx-background-color: #2ea043;
                    }

                    /* ====== FORM FIELDS ====== */
                    .text-field, .password-field, .combo-box {
                        -fx-background-color: #0d1117;
                        -fx-text-fill: #e6edf3;
                        -fx-border-color: #30363d;
                        -fx-border-radius: 8;
                        -fx-background-radius: 8;
                        -fx-padding: 8;
                    }

                    .text-field:focused, .password-field:focused {
                        -fx-border-color: #1f6feb;
                    }

                    /* ====== CHARTS ====== */
                    .chart {
                        -fx-background-color: #21262d;
                        -fx-background-radius: 10;
                        -fx-padding: 10;
                    }

                    .chart-title {
                        -fx-text-fill: #e6edf3;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                    }

                    .chart-legend-item {
                        -fx-text-fill: #e6edf3;
                    }

                    .chart-legend {
                        -fx-background-color: #21262d;
                    }

                    /* ====== TABS ====== */
                    .tab-pane {
                        -fx-background-color: #121212;
                    }

                    .tab {
                        -fx-background-color: #21262d;
                        -fx-text-fill: #8b949e;
                    }

                    .tab:selected {
                        -fx-background-color: #1f6feb;
                    }

                    .tab:selected .tab-label {
                        -fx-text-fill: white;
                    }

                    .tab-pane .tab-header-area {
                        -fx-background-color: #121212;
                    }

                    /* ====== SCROLL PANE ====== */
                    .scroll-pane {
                        -fx-background-color: #121212;
                    }

                    .scroll-pane .viewport {
                        -fx-background-color: #121212;
                    }

                    /* ====== SEPARATOR ====== */
                    .separator .line {
                        -fx-background-color: #30363d;
                    }

                    /* ====== PROGRESS BAR ====== */
                    .progress-bar {
                        -fx-accent: #1f6feb;
                    }

                    .progress-bar .bar {
                        -fx-background-color: #1f6feb;
                        -fx-background-radius: 10;
                    }

                    .progress-bar .track {
                        -fx-background-color: #21262d;
                        -fx-background-radius: 10;
                    }

                    /* ====== DIALOGS ====== */
                    .dialog-pane {
                        -fx-background-color: #21262d;
                    }

                    .dialog-pane .label {
                        -fx-text-fill: #e6edf3;
                    }

                    .dialog-pane .header-panel {
                        -fx-background-color: #161b22;
                    }

                    /* ====== HYPERLINK ====== */
                    .hyperlink {
                        -fx-text-fill: #58a6ff;
                    }

                    .hyperlink:hover {
                        -fx-text-fill: #79c0ff;
                    }

                    /* ====== COMBOBOX POPUP ====== */
                    .combo-box-popup .list-view {
                        -fx-background-color: #21262d;
                    }

                    .combo-box-popup .list-cell {
                        -fx-text-fill: #e6edf3;
                        -fx-background-color: #21262d;
                    }

                    .combo-box-popup .list-cell:hover {
                        -fx-background-color: #2d3a4a;
                    }

                    /* ====== TOOLTIP ====== */
                    .tooltip {
                        -fx-background-color: #21262d;
                        -fx-text-fill: #e6edf3;
                        -fx-border-color: #30363d;
                    }
                    """;
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add("data:text/css," + darkStyle.replace("\n", ""));
        } else {
            // Light Mode
            currentScene.setFill(Color.web("#f4f6f9"));
            rootLayout.setStyle("-fx-background-color: #f4f6f9;");
            currentScene.getStylesheets().clear();
            try {
                String cssPath = "src/main/resources/css/styles.css";
                java.io.File cssFile = new java.io.File(cssPath);
                if (cssFile.exists()) {
                    currentScene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
                }
            } catch (Exception e) {
                System.out.println("Error loading light theme: " + e.getMessage());
            }
        }
    }

    public static void refreshCurrentView() {
        if (rootLayout != null) {
            applyTheme();
        }
    }
}