package database;


import java.util.List;
import java.util.stream.Collectors;

import model.Budget;

public class BudgetDAO {
    
    final List<Budget> usersBudgets = List.of();

    public void insertBudget(Budget budget) {
        usersBudgets.add(budget);
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        return usersBudgets.stream().filter(budget-> budget.getUserId()==userId).collect(Collectors.toList());
    }

    public void updateBudget(Budget budget) {
        
    }

    public void deleteBudget(int id) {
        
    }
}