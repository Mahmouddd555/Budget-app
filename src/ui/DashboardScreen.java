package ui;

import controller.BudgetController;
import controller.GoalController;
import controller.TransactionController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardScreen {

    private final User currentUser;
    private final TransactionController transactionController;
    private final BudgetController budgetController;
    private final GoalController goalController;

    public DashboardScreen(User user) {
        this.currentUser = user;
        this.transactionController = new TransactionController();
        this.budgetController = new BudgetController();
        this.goalController = new GoalController();
    }

    public void show() {

        JFrame frame = new JFrame("Dashboard — " + currentUser.getName());
        frame.setSize(650, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(245, 246, 250));

        // Header
        JLabel header = new JLabel("Welcome, " + currentUser.getName(), SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Get user transactions
        List<Transaction> txs = transactionController.getUserTransactions(currentUser);

        // FIXED CALCULATION 🔥
        double income = txs.stream()
                .filter(t -> t.getUserId() == currentUser.getId())
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = txs.stream()
                .filter(t -> t.getUserId() == currentUser.getId())
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = income - expense;

        // Summary cards
        JPanel summary = new JPanel(new GridLayout(1, 3, 10, 10));
        summary.setBackground(new Color(245, 246, 250));

        summary.add(card("Balance", balance, new Color(41, 128, 185)));
        summary.add(card("Income", income, new Color(39, 174, 96)));
        summary.add(card("Expense", expense, new Color(192, 57, 43)));

        // Buttons (رجّعنا كل حاجة زي ما كانت)
        JButton addTx = new JButton("Add Transaction");
        JButton budget = new JButton("Budgets");
        JButton goals = new JButton("Goals");
        JButton logout = new JButton("Logout");

        style(addTx, new Color(41, 128, 185));
        style(budget, new Color(39, 174, 96));
        style(goals, new Color(52, 152, 219));
        style(logout, new Color(231, 76, 60));

        addTx.addActionListener(e -> new AddTransactionScreen(currentUser).show());
        budget.addActionListener(e -> new BudgetsScreen(currentUser).show());
        goals.addActionListener(e -> new GoalsScreen(currentUser).show());
        logout.addActionListener(e -> {
            frame.dispose();
            new LoginScreen().show();
        });

        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));
        buttons.setBackground(new Color(245, 246, 250));
        buttons.add(addTx);
        buttons.add(budget);
        buttons.add(goals);
        buttons.add(logout);

        // Recent transactions table
        String[] cols = { "Date", "Desc", "Category", "Type", "Amount" };

        Object[][] data = txs.stream()
                .filter(t -> t.getUserId() == currentUser.getId())
                .limit(5)
                .map(t -> new Object[] {
                        t.getDate(),
                        t.getDescription(),
                        t.getCategory(),
                        t.getType(),
                        t.getAmount()
                })
                .toArray(Object[][]::new);

        JTable table = new JTable(data, cols);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(245, 246, 250));
        main.add(summary, BorderLayout.NORTH);
        main.add(buttons, BorderLayout.CENTER);
        main.add(scroll, BorderLayout.SOUTH);

        frame.setLayout(new BorderLayout());
        frame.add(header, BorderLayout.NORTH);
        frame.add(main, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel card(String title, double value, Color color) {

        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(color);

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        JLabel v = new JLabel(String.format("%.2f", value), SwingConstants.CENTER);

        t.setForeground(Color.WHITE);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("SansSerif", Font.BOLD, 16));

        p.add(t);
        p.add(v);

        return p;
    }

    private void style(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
    }
}