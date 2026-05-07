package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {

        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "password TEXT" +
                ");";

        String transactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "amount REAL," +
                "date TEXT," +
                "description TEXT," +
                "category TEXT," +
                "type TEXT" +
                ");";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(usersTable);
            stmt.execute(transactionsTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}