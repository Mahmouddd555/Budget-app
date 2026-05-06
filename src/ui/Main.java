package ui;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> startApp());
    }

    private static void startApp() {
        showLogin();
    }

    private static void showLogin() {
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.show();
    }
}