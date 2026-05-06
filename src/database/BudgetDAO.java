package database;


import java.util.List;
import java.util.stream.Collectors;

import model.Budget;


public class BudgetDAO {
    
    public void addBudget(Budget budget) {
        Database.budgets.add(budget);
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        return Database.budgets.stream().filter(budget -> budget.getId() == userId).collect(Collectors.toList());
    }

    public void updateBudget(Budget budget) {
        Database.budgets.remove(budget);
        Database.budgets.add(budget);
    }

    public void deleteBudget(int id) {
        Budget budget = Database.budgets.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
        if (budget != null) {
            Database.budgets.remove(budget);
        }
    }

}