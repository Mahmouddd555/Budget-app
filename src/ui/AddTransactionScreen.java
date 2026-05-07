package ui;

import controller.TransactionController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransactionScreen extends JFrame {

    private final User currentUser;
    private final TransactionController controller;

    private static final String[] INCOME = { "Salary", "Freelance", "Gift", "Other" };
    private static final String[] EXPENSE = { "Food", "Transport", "Bills", "Shopping", "Other" };

    public AddTransactionScreen(User user) {
        this.currentUser = user;
        this.controller = new TransactionController();
    }

    public void show() {

        JFrame frame = new JFrame("Add Transaction");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JButton income = new JButton("Income");
        JButton expense = new JButton("Expense");

        income.addActionListener(e -> openForm(frame, true));
        expense.addActionListener(e -> openForm(frame, false));

        JPanel p = new JPanel(new GridLayout(2, 1));
        p.add(income);
        p.add(expense);

        frame.add(p);
        frame.setVisible(true);
    }

    private void openForm(JFrame parent, boolean isIncome) {

        parent.dispose();

        JFrame frame = new JFrame(isIncome ? "Income" : "Expense");
        frame.setSize(350, 250);
        frame.setLocationRelativeTo(null);

        JTextField amount = new JTextField();
        JTextField desc = new JTextField();
        JComboBox<String> cat = new JComboBox<>(isIncome ? INCOME : EXPENSE);

        JButton save = new JButton("Save");

        save.addActionListener(e -> {

            double value;

            try {
                value = Double.parseDouble(amount.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number");
                return;
            }

            if (isIncome) {
                controller.addIncome(
                        currentUser.getId(),
                        value,
                        LocalDate.now(),
                        desc.getText(),
                        cat.getSelectedItem().toString());
            } else {
                controller.addExpense(
                        currentUser.getId(),
                        value,
                        LocalDate.now(),
                        desc.getText(),
                        cat.getSelectedItem().toString());
            }

            JOptionPane.showMessageDialog(frame, "Saved Successfully");

            frame.dispose();

            // 🔥 أهم سطر: يرجّع تحديث للدashboard
            new DashboardScreen(currentUser).show();
        });

        JPanel p = new JPanel(new GridLayout(5, 1));
        p.add(new JLabel("Amount"));
        p.add(amount);
        p.add(new JLabel("Description"));
        p.add(desc);
        p.add(cat);
        p.add(save);

        frame.add(p);
        frame.setVisible(true);
    }
}