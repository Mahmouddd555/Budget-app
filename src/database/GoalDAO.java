package database;

import model.Goal;

import java.util.List;
import java.util.stream.Collectors;

public class GoalDAO {

    public void save(Goal goal) {
        Database.goals.add(goal);
    }
    
    public Goal findById(int Id) {

        return Database.goals.stream().filter(goal -> goal.getId() == Id).collect(Collectors.toList()).get(0);
    }

    public void update(Goal goal) {
        Goal existingGoal = Database.goals.stream().filter(g -> g.getId() == goal.getId()).findFirst().orElse(null);
        if (existingGoal != null) {
            Database.goals.remove(existingGoal);
            Database.goals.add(goal);
        }
    }

    public void deleteGoal(int id) {
        Goal goal = Database.goals.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
        if (goal != null) {
            Database.goals.remove(goal);
        }
    }

    public void setCurrentAmount(int id, double amount) {
        Goal goal = Database.goals.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
        if (goal != null) {
            goal.setCurrentAmount(amount);
            update(goal);
        }
    }

    public List<Goal> findByUserId(int userId) {
        return Database.goals.stream()
                .filter(goal -> goal.getId() == userId)
                .collect(Collectors.toList());
    }
}