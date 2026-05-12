package com.budget.database;

import com.budget.models.Goal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    public boolean createGoal(Goal goal) {
        String sql = "INSERT INTO goals (user_id, name, target_amount, saved_amount, target_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, goal.getUserId());
            pstmt.setString(2, goal.getName());
            pstmt.setDouble(3, goal.getTargetAmount());
            pstmt.setDouble(4, goal.getSavedAmount());
            pstmt.setDate(5, Date.valueOf(goal.getTargetDate()));
            pstmt.setString(6, goal.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    goal.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating goal: " + e.getMessage());
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

    public List<Goal> getGoalsByUserId(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return goals;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Goal goal = new Goal();
                goal.setId(rs.getInt("id"));
                goal.setUserId(rs.getInt("user_id"));
                goal.setName(rs.getString("name"));
                goal.setTargetAmount(rs.getDouble("target_amount"));
                goal.setSavedAmount(rs.getDouble("saved_amount"));
                goal.setTargetDate(rs.getDate("target_date").toLocalDate());
                goal.setStatus(rs.getString("status"));
                goals.add(goal);
            }
        } catch (SQLException e) {
            System.err.println("Error getting goals: " + e.getMessage());
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
        return goals;
    }

    public boolean updateSavedAmount(int goalId, double newSavedAmount) {
        String sql = "UPDATE goals SET saved_amount = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newSavedAmount);
            pstmt.setInt(2, goalId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating saved amount: " + e.getMessage());
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

    public boolean updateGoalStatus(int goalId, String status) {
        String sql = "UPDATE goals SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, goalId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating goal status: " + e.getMessage());
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

    public boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM goals WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
                return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, goalId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting goal: " + e.getMessage());
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