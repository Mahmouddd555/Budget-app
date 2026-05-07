package database;

import model.Goal;
import java.util.List;
import java.util.stream.Collectors;

public class GoalDAO {

    public void save(Goal goal) {
        Database.goals.add(goal);
    }

    public Goal findById(int id) {
        return Database.goals.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(Goal goal) {
        Goal existing = findById(goal.getId());
        if (existing != null) {
            Database.goals.remove(existing);
            Database.goals.add(goal);
        }
    }

    public void deleteGoal(int id) {
        Goal goal = findById(id);
        if (goal != null) {
            Database.goals.remove(goal);
        }
    }

    public void setCurrentAmount(int id, double amount) {
        Goal goal = findById(id);
        if (goal != null) {
            goal.setCurrentAmount(amount);
        }
    }

    public List<Goal> getAllGoals() {
        return Database.goals;
    }

    // ✅ FIXED IMPORTANT BUG
    public List<Goal> findByUserId(int userId) {
        return Database.goals.stream()
                .filter(g -> g.getUserId() == userId)
                .collect(Collectors.toList());
    }
}