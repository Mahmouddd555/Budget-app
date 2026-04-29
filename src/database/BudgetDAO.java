package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Budget;

public class BudgetDAO {

    public void insertBudget(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, amount, category) VALUES (?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, budget.getUserId());
            stmt.setDouble(2, budget.getAmount());
            stmt.setString(3, budget.getCategory());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                budgets.add(new Budget(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("category")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return budgets;
    }

    public void updateBudget(Budget budget) {
        String sql = "UPDATE budgets SET amount = ?, category = ? WHERE id = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, budget.getAmount());
            stmt.setString(2, budget.getCategory());
            stmt.setInt(3, budget.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBudget(int id) {
        String sql = "DELETE FROM budgets WHERE id = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}