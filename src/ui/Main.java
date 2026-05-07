package ui;

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {

        // 🔥 تشغيل الداتابيز (مرة واحدة عند بدء البرنامج)
        DatabaseInitializer.init();

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