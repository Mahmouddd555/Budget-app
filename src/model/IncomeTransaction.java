package model;

import java.time.LocalDate;

public class IncomeTransaction extends Transaction {

    public IncomeTransaction(int userId, double amount, LocalDate date, String description, String category) {
        super(userId, amount, date, description, category);
    }

    @Override
    public String getType() {
        return "Income";
    }
}