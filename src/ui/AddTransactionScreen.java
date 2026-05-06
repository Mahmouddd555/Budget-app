package ui;

import controller.TransactionController;
import model.User;
import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;

public class AddTransactionScreen extends JFrame {

    private User currentUser;
    private TransactionController transactionController;

    private static final String[] INCOME_CATEGORIES = {
            "Salary", "Freelance", "Investment", "Gift", "Other"
    };
    private static final String[] EXPENSE_CATEGORIES = {
            "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other"
    };

    public AddTransactionScreen(User user) {
        this.currentUser = user;
        this.transactionController = new TransactionController();
    }

    // ─── show() ──────────────────────────────────────────────────────────────

    public void show() {
        JFrame frame = new JFrame("Add New Transaction");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel label = new JLabel("Choose transaction type:", SwingConstants.CENTER);
        JButton incomeBtn = new JButton("Income");
        JButton expenseBtn = new JButton("Expense");

        incomeBtn.addActionListener(e -> {
            frame.dispose();
            handleAddIncome();
        });
        expenseBtn.addActionListener(e -> {
            frame.dispose();
            handleAddExpense();
        });

        panel.add(label);
        panel.add(incomeBtn);
        panel.add(expenseBtn);

        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── handleAddIncome() ───────────────────────────────────────────────────

    private void handleAddIncome() {
        JFrame frame = new JFrame("Add Income");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField amountField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(INCOME_CATEGORIES);
        JTextField descField = new JTextField();

        panel.add(new JLabel("Amount ($):"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel(""));

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            double amount = validateAmount(amountField.getText());
            if (amount < 0) return;

            String category = (String) categoryBox.getSelectedItem();
            String description = descField.getText().trim();

            transactionController.addIncome(
                    currentUser.getId(), amount, LocalDate.now(), description, category
            );
            frame.dispose();
            showSuccess("Income of $" + String.format("%.2f", amount) + " added successfully!", frame);
        });

        panel.add(saveBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── handleAddExpense() ──────────────────────────────────────────────────

    private void handleAddExpense() {
        JFrame frame = new JFrame("Add Expense");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField amountField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(EXPENSE_CATEGORIES);
        JTextField descField = new JTextField();

        panel.add(new JLabel("Amount ($):"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel(""));

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            double amount = validateAmount(amountField.getText());
            if (amount < 0) return;

            String category = (String) categoryBox.getSelectedItem();
            String description = descField.getText().trim();

            transactionController.addExpense(
                    currentUser.getId(), amount, LocalDate.now(), description, category
            );
            frame.dispose();
            showSuccess("Expense of $" + String.format("%.2f", amount) + " recorded successfully!", frame);
        });

        panel.add(saveBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── selectCategory() → JComboBox (مدمجة في الـ forms فوق) ──────────────

    private String selectCategory(String[] categories) {
        // مش محتاجينها منفصلة في Swing — الـ JComboBox بيعملها مباشرة في الـ form
        return (String) JOptionPane.showInputDialog(
                null,
                "Select a category:",
                "Category",
                JOptionPane.PLAIN_MESSAGE,
                null,
                categories,
                categories[0]
        );
    }

    // ─── validateAmount() ────────────────────────────────────────────────────

    private double validateAmount(String input) {
        try {
            double amount = Double.parseDouble(input.trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Amount must be greater than zero.",
                        "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid number.",
                    "Invalid Amount", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    // ─── showSuccess() ───────────────────────────────────────────────────────

    private void showSuccess(String message, JFrame parentFrame) {
        int choice = JOptionPane.showConfirmDialog(
                null,
                message + "\n\nAdd another transaction?",
                "Success ✔",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) show();
    }
}