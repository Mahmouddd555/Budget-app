package com.budget.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("❌ Failed to connect to database");
                return;
            }

            stmt = conn.createStatement();

            // Create Users table
            String createUsersTable = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT UNIQUE NOT NULL,
                            email TEXT UNIQUE NOT NULL,
                            password_hash TEXT NOT NULL,
                            total_balance REAL DEFAULT 0,
                            monthly_income REAL DEFAULT 0,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                    """;
            stmt.execute(createUsersTable);

            // Create Transactions table
            String createTransactionsTable = """
                        CREATE TABLE IF NOT EXISTS transactions (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            type TEXT NOT NULL CHECK(type IN ('INCOME', 'EXPENSE')),
                            amount REAL NOT NULL,
                            category TEXT NOT NULL,
                            description TEXT,
                            date DATE NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
            stmt.execute(createTransactionsTable);

            // Create Budgets table
            String createBudgetsTable = """
                        CREATE TABLE IF NOT EXISTS budgets (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            category TEXT NOT NULL,
                            allocated_amount REAL NOT NULL,
                            spent_amount REAL DEFAULT 0,
                            period TEXT NOT NULL CHECK(period IN ('DAILY', 'WEEKLY', 'MONTHLY')),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
            stmt.execute(createBudgetsTable);

            // Create Goals table
            String createGoalsTable = """
                        CREATE TABLE IF NOT EXISTS goals (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            name TEXT NOT NULL,
                            target_amount REAL NOT NULL,
                            saved_amount REAL DEFAULT 0,
                            target_date DATE NOT NULL,
                            status TEXT DEFAULT 'ACTIVE',
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                        )
                    """;
            stmt.execute(createGoalsTable);

            System.out.println("✅ Database tables initialized successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Database initialization error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}