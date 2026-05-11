package ui;

import javax.swing.SwingUtilities;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.init();
        SwingUtilities.invokeLater(Main::showLogin);
    }

    private static void showLogin() {
        new LoginScreen().show();
    }
}
