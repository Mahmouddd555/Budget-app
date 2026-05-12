package com.budget.database;

import com.budget.models.Budget;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    public boolean createBudget(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, category, allocated_amount, spent_amount, period) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, budget.getUserId());
            pstmt.setString(2, budget.getCategory());
            pstmt.setDouble(3, budget.getAllocatedAmount());
            pstmt.setDouble(4, budget.getSpentAmount());
            pstmt.setString(5, budget.getPeriod());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    budget.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating budget: " + e.getMessage());
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }
        return false;
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return budgets;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Budget budget = new Budget();
                budget.setId(rs.getInt("id"));
                budget.setUserId(rs.getInt("user_id"));
                budget.setCategory(rs.getString("category"));
                budget.setAllocatedAmount(rs.getDouble("allocated_amount"));
                budget.setSpentAmount(rs.getDouble("spent_amount"));
                budget.setPeriod(rs.getString("period"));
                budgets.add(budget);
            }
        } catch (SQLException e) {
            System.err.println("Error getting budgets: " + e.getMessage());
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }
        return budgets;
    }

    public boolean updateSpentAmount(int budgetId, double newSpentAmount) {
        String sql = "UPDATE budgets SET spent_amount = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newSpentAmount);
            pstmt.setInt(2, budgetId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating spent amount: " + e.getMessage());
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }
        return false;
    }

    public boolean deleteBudget(int budgetId) {
        String sql = "DELETE FROM budgets WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, budgetId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting budget: " + e.getMessage());
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }
        return false;
    }
}