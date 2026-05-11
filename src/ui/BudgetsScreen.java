package ui;

import controller.BudgetController;
import model.Budget;
import model.Transaction;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class BudgetsScreen {

    private static final String[] CATEGORIES = {
            "Food", "Transport", "Bills", "Shopping", "Entertainment"
    };

    private final User currentUser;
    private final BudgetController budgetController;

    public BudgetsScreen(User user) {
        this.currentUser = user;
        this.budgetController = new BudgetController();
    }

    public void show() {
        JFrame frame = new JFrame("Masroofy — Budgets");
        frame.setSize(1050, 680);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BG);
        frame.setLayout(new BorderLayout());

        frame.add(new TopNavBar(currentUser, SideBar.Page.BUDGETS), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(UITheme.BG);
        body.add(new SideBar(currentUser, SideBar.Page.BUDGETS), BorderLayout.WEST);
        body.add(buildContent(frame), BorderLayout.CENTER);

        frame.add(body, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel buildContent(JFrame frame) {

        JPanel root = new JPanel();
        root.setBackground(UITheme.BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(UITheme.pagePadding());

        JLabel title = UITheme.label("Budgets", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = UITheme.label(
                "Manage and track your spending limits",
                UITheme.FONT_BODY,
                UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        StyledButton addBtn = new StyledButton("➕ New Budget", StyledButton.Variant.PRIMARY);
        addBtn.setPreferredSize(new Dimension(150, 38));

        addBtn.addActionListener(e -> addBudgetDialog(frame));

        topRow.add(addBtn, BorderLayout.EAST);

        // ── Data ──
        List<Budget> budgets = budgetController.getAllBudgets();
        List<Transaction> transactions = budgetController.getTransactionsByUserId(currentUser.getId());

        String[] cols = { "Category", "Budget Limit", "Spent", "Remaining", "Usage", "Status" };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Budget b : budgets) {

            // SAFE user check (بدل id لو عندك userId استخدمه)
            if (b.getId() == currentUser.getId()) {

                double spent = transactions.stream()
                        .filter(t -> t.getCategory() != null && t.getCategory().equals(b.getCategory()))
                        .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                        .mapToDouble(Transaction::getAmount)
                        .sum();

                double left = b.getAmount() - spent;
                double percent = b.getAmount() > 0 ? (spent / b.getAmount()) * 100 : 0;

                String status = percent > 100 ? "Over Budget"
                        : percent > 90 ? "Near Limit"
                                : "OK";

                model.addRow(new Object[] {
                        b.getCategory(),
                        String.format("%,.2f EGP", b.getAmount()),
                        String.format("%,.2f EGP", spent),
                        String.format("%,.2f EGP", left),
                        String.format("%.1f%%", percent),
                        status
                });
            }
        }

        JTable table = buildTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scroll.getViewport().setBackground(UITheme.SURFACE);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        root.add(title);
        root.add(Box.createVerticalStrut(4));
        root.add(sub);
        root.add(Box.createVerticalStrut(16));
        root.add(topRow);
        root.add(Box.createVerticalStrut(14));
        root.add(UITheme.label("Budget Progress", UITheme.FONT_H2, UITheme.TEXT_PRIMARY));
        root.add(Box.createVerticalStrut(10));
        root.add(scroll);

        JScrollPane outer = new JScrollPane(root);
        outer.setBorder(null);
        outer.getViewport().setBackground(UITheme.BG);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(UITheme.BG);
        wrap.add(outer);

        return wrap;
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        table.setFont(UITheme.FONT_BODY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(UITheme.SURFACE);
        table.setRowHeight(38);
        table.setShowHorizontalLines(true);
        table.setGridColor(UITheme.BORDER);

        JTableHeader header = table.getTableHeader();
        header.setFont(UITheme.FONT_LABEL);

        table.getColumnModel().getColumn(5)
                .setCellRenderer(new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(
                            JTable t, Object v, boolean sel, boolean foc, int r, int c) {

                        JLabel l = new JLabel(v.toString());
                        l.setOpaque(true);
                        l.setHorizontalAlignment(CENTER);

                        String s = v.toString();

                        if (s.equals("Over Budget")) {
                            l.setBackground(UITheme.DANGER_SOFT);
                            l.setForeground(UITheme.DANGER);
                        } else if (s.equals("Near Limit")) {
                            l.setBackground(UITheme.WARNING_SOFT);
                            l.setForeground(UITheme.WARNING);
                        } else {
                            l.setBackground(UITheme.SUCCESS_SOFT);
                            l.setForeground(UITheme.SUCCESS);
                        }

                        return l;
                    }
                });

        return table;
    }

    private void addBudgetDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "New Budget", true);
        dialog.setSize(380, 240);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(UITheme.SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> catBox = new JComboBox<>(CATEGORIES);
        JTextField limitField = new JTextField();

        JButton saveBtn = new JButton("Save Budget");

        saveBtn.addActionListener(e -> {
            try {
                double limit = Double.parseDouble(limitField.getText().trim());
                if (limit <= 0)
                    return;

                budgetController.addBudget(
                        currentUser.getId(),
                        limit,
                        (String) catBox.getSelectedItem());

                dialog.dispose();
                parent.dispose();
                new BudgetsScreen(currentUser).show();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input");
            }
        });

        card.add(new JLabel("Category"));
        card.add(catBox);
        card.add(new JLabel("Limit"));
        card.add(limitField);
        card.add(saveBtn);

        dialog.add(card);
        dialog.setVisible(true);
    }
}