package model;

import java.time.LocalDate;

public class ExpenseTransaction extends Transaction {

    public ExpenseTransaction(int id, double amount, LocalDate date, String description, Category category) {
        super(id, amount, date, description, category);
    }

    @Override
    public String getType() {}
}