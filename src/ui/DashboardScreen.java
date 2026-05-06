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
        this.currentUser          = user;
        this.transactionController = new TransactionController();
        this.budgetController      = new BudgetController();
        this.goalController        = new GoalController();
    }

    public void show() {
        JFrame frame = new JFrame("Dashboard — " + currentUser.getName());
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ── Header ──────────────────────────────────────────────────────────
        JLabel header = new JLabel("Welcome, " + currentUser.getName() + "!", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));

        // ── Summary panel ───────────────────────────────────────────────────
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        List<Transaction> txs = transactionController.getUserTransactions(currentUser);
        double income  = txs.stream().filter(t -> t.getType().equals("Income")).mapToDouble(Transaction::getAmount).sum();
        double expense = txs.stream().filter(t -> t.getType().equals("Expense")).mapToDouble(Transaction::getAmount).sum();
        double balance = income - expense;

        summaryPanel.add(makeSummaryCard("Balance",  String.format("$%.2f", balance),  new Color(33, 97, 140)));
        summaryPanel.add(makeSummaryCard("Income",   String.format("+$%.2f", income),  new Color(39, 174, 96)));
        summaryPanel.add(makeSummaryCard("Expenses", String.format("-$%.2f", expense), new Color(192, 57, 43)));

        // ── Nav buttons ─────────────────────────────────────────────────────
        JPanel navPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton txBtn      = new JButton("Add Transaction");
        JButton budgetBtn  = new JButton("Manage Budgets");
        JButton goalsBtn   = new JButton("Manage Goals");
        JButton logoutBtn  = new JButton("Logout");

        txBtn.addActionListener(e -> new AddTransactionScreen(currentUser).show());
        budgetBtn.addActionListener(e -> new BudgetsScreen(currentUser).show());
        goalsBtn.addActionListener(e -> new GoalsScreen(currentUser).show());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginScreen().show();
        });

        for (JButton btn : new JButton[]{txBtn, budgetBtn, goalsBtn}) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setPreferredSize(new Dimension(0, 60));
            navPanel.add(btn);
        }
        logoutBtn.setForeground(Color.RED);
        navPanel.add(logoutBtn);

        // ── Recent transactions ──────────────────────────────────────────────
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel recentLabel = new JLabel("Recent Transactions");
        recentLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        String[] cols = {"Date", "Description", "Category", "Type", "Amount"};
        Object[][] rows = txs.stream().limit(5).map(t -> new Object[]{
                t.getDate(), t.getDescription(), t.getCategory(), t.getType(),
                String.format("$%.2f", t.getAmount())
        }).toArray(Object[][]::new);

        JTable table = new JTable(rows, cols);
        table.setEnabled(false);
        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ── Assemble ────────────────────────────────────────────────────────
        frame.setLayout(new BorderLayout(0, 0));
        frame.add(header,       BorderLayout.NORTH);
        frame.add(summaryPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(summaryPanel, BorderLayout.NORTH);
        center.add(navPanel,     BorderLayout.CENTER);
        center.add(recentPanel,  BorderLayout.SOUTH);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel makeSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
        titleLbl.setForeground(Color.WHITE);
        valueLbl.setForeground(Color.WHITE);
        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 16));

        card.add(titleLbl);
        card.add(valueLbl);
        return card;
    }
}