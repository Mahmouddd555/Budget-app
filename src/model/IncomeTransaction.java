package model;

import java.time.LocalDate;

public class IncomeTransaction extends Transaction {

    public IncomeTransaction(int id, double amount, LocalDate date, String description, Category category) {
        super(id, amount, date, description, category);
    }

    @Override
    public String getType() {}
}