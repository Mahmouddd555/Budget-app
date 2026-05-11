package ui;

import controller.BudgetController;
import controller.GoalController;
import controller.TransactionController;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
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
        JFrame frame = new JFrame("Masroofy — Dashboard");
        frame.setSize(1050, 680);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BG);
        frame.setLayout(new BorderLayout());

        // Top + Sidebar
        frame.add(new TopNavBar(currentUser, SideBar.Page.DASHBOARD), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(UITheme.BG);
        body.add(new SideBar(currentUser, SideBar.Page.DASHBOARD), BorderLayout.WEST);
        body.add(buildContent(frame), BorderLayout.CENTER);

        frame.add(body, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel buildContent(JFrame frame) {

        List<Transaction> txs = transactionController.getUserTransactions(currentUser);

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

        JPanel root = new JPanel();
        root.setBackground(UITheme.BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(UITheme.pagePadding());

        // Greeting
        JLabel greeting = UITheme.label(
                "Good morning, " + currentUser.getName() + " 👋",
                UITheme.FONT_TITLE,
                UITheme.TEXT_PRIMARY);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = UITheme.label(
                "Here's your financial overview",
                UITheme.FONT_BODY,
                UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        root.add(greeting);
        root.add(Box.createVerticalStrut(4));
        root.add(sub);
        root.add(Box.createVerticalStrut(20));

        // Cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 0));
        cards.setBackground(UITheme.BG);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        cards.add(statCard("Balance", balance, UITheme.PRIMARY));
        cards.add(statCard("Income", income, UITheme.SUCCESS));
        cards.add(statCard("Expenses", expense, UITheme.DANGER));

        root.add(cards);
        root.add(Box.createVerticalStrut(18));

        // Buttons
        JPanel qbtns = new JPanel(new GridLayout(1, 3, 10, 0));
        qbtns.setBackground(UITheme.BG);
        qbtns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        qbtns.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton addTx = new StyledButton("➕ Add Transaction", StyledButton.Variant.PRIMARY);
        StyledButton budgets = new StyledButton("📊 Budgets", StyledButton.Variant.SUCCESS);
        StyledButton goals = new StyledButton("🎯 Goals", StyledButton.Variant.PRIMARY);

        addTx.addActionListener(e -> {
            frame.dispose();
            new AddTransactionScreen(currentUser).show();
        });

        budgets.addActionListener(e -> {
            frame.dispose();
            new BudgetsScreen(currentUser).show();
        });

        goals.addActionListener(e -> {
            frame.dispose();
            new GoalsScreen(currentUser).show();
        });

        qbtns.add(addTx);
        qbtns.add(budgets);
        qbtns.add(goals);

        root.add(qbtns);
        root.add(Box.createVerticalStrut(20));

        // Table title
        JLabel tblTitle = UITheme.label(
                "Recent Transactions",
                UITheme.FONT_H2,
                UITheme.TEXT_PRIMARY);
        tblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(tblTitle);
        root.add(Box.createVerticalStrut(10));

        Object[][] data = txs.stream()
                .filter(t -> t.getUserId() == currentUser.getId())
                .limit(8)
                .map(t -> new Object[] {
                        t.getDate(),
                        t.getDescription(),
                        t.getCategory(),
                        t.getType(),
                        t.getAmount()
                })
                .toArray(Object[][]::new);

        String[] cols = { "Date", "Description", "Category", "Type", "Amount" };

        JTable table = buildTable(data, cols);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scroll.getViewport().setBackground(UITheme.SURFACE);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        root.add(scroll);

        JScrollPane outer = new JScrollPane(root);
        outer.setBorder(null);
        outer.getViewport().setBackground(UITheme.BG);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UITheme.BG);
        wrapper.add(outer);

        return wrapper;
    }

    private JPanel statCard(String title, double value, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UITheme.SURFACE);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel t = UITheme.label(title, UITheme.FONT_LABEL, UITheme.TEXT_SECONDARY);
        JLabel v = UITheme.label(String.format("%,.2f EGP", value), UITheme.FONT_H2, accent);

        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);

        return p;
    }

    private JTable buildTable(Object[][] data, String[] cols) {

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(UITheme.FONT_BODY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(UITheme.SURFACE);
        table.setRowHeight(36);
        table.setGridColor(UITheme.BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(UITheme.PRIMARY_SOFT);

        JTableHeader header = table.getTableHeader();
        header.setFont(UITheme.FONT_LABEL);
        header.setForeground(UITheme.TEXT_SECONDARY);
        header.setBackground(new Color(250, 251, 253));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

        return table;
    }
}