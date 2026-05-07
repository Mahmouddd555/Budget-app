package ui;

import controller.BudgetController;
import model.Budget;
import model.Transaction;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BudgetsScreen extends JFrame {

    private static final String[] CATEGORIES = {"Food", "Transport", "Bills", "Shopping", "Entertainment"};

    private final User currentUser;
    private final BudgetController budgetController;

    public BudgetsScreen(User user) {
        this.currentUser = user;
        this.budgetController = new BudgetController();
    }

    // ─── show() ──────────────────────────────────────────────────────────────

    public void show() {
        JFrame frame = new JFrame("Budgets Management");
        frame.setSize(380, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton viewBtn     = new JButton("View Budgets");
        JButton addBtn      = new JButton("Add Budget");
        JButton progressBtn = new JButton("Show Progress");

        viewBtn.addActionListener(e -> { frame.dispose(); viewBudgets(); });
        addBtn.addActionListener(e -> { frame.dispose(); addBudget(); });
        progressBtn.addActionListener(e -> { frame.dispose(); showProgress(); });

        panel.add(viewBtn);
        panel.add(addBtn);
        panel.add(progressBtn);
        panel.add(new JLabel(""));

        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── viewBudgets() ───────────────────────────────────────────────────────

    private void viewBudgets() {
        List<Budget> budgets = budgetController.getAllBudgets();

        String[] columns = {"Category", "Budget Limit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Budget b : budgets) {
            if (b.getId() == currentUser.getId()) {
                model.addRow(new Object[]{b.getCategory(), String.format("$%.2f", b.getAmount())});
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("My Budgets");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { frame.dispose(); show(); });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // ─── addBudget() ─────────────────────────────────────────────────────────

    private void addBudget() {
        JFrame frame = new JFrame("Add Budget");
        frame.setSize(350, 220);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
        JTextField limitField = new JTextField();

        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Budget Limit ($):"));
        panel.add(limitField);
        panel.add(new JLabel(""));

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            double limit;
            try {
                limit = Double.parseDouble(limitField.getText().trim());
                if (limit <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Limit must be greater than zero.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid number.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            budgetController.addBudget(currentUser.getId(), limit, category);

            int choice = JOptionPane.showConfirmDialog(frame,
                    "Budget added for " + category + "!\n\nAdd another?",
                    "Success ✔", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            if (choice == JOptionPane.YES_OPTION) addBudget();
            else show();
        });

        panel.add(saveBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── showProgress() ──────────────────────────────────────────────────────

    private void showProgress() {
        List<Budget> budgets = budgetController.getAllBudgets();
        List<Transaction> transactions = budgetController.getTransactionsByUserId(currentUser.getId());

        String[] columns = {"Category", "Budget", "Spent", "Left", "%", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Budget b : budgets) {
            if (b.getId() == currentUser.getId()) {
                double spent = transactions.stream()
                        .filter(t -> t.getCategory().equals(b.getCategory()) && t.getType().equals("Expense"))
                        .mapToDouble(Transaction::getAmount)
                        .sum();
                double left    = b.getAmount() - spent;
                double percent = (spent / b.getAmount()) * 100;

                String status = percent > 100 ? "⚠ Over Budget!"
                        : percent > 90 ? "⚠ Near Limit"
                        : "✔ OK";

                model.addRow(new Object[]{
                        b.getCategory(),
                        String.format("$%.2f", b.getAmount()),
                        String.format("$%.2f", spent),
                        String.format("$%.2f", left),
                        String.format("%.1f%%", percent),
                        status
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Budget Progress");
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { frame.dispose(); show(); });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}