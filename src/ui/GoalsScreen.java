package ui;

import controller.GoalController;
import model.Goal;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GoalsScreen {

    private final User currentUser;
    private final GoalController goalController;

    private JPanel contentArea;

    public GoalsScreen(User user) {
        this.currentUser = user;
        this.goalController = new GoalController();
    }

    public void show() {

        JFrame frame = new JFrame("Masroofy — Goals");
        frame.setSize(1050, 680);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BG);
        frame.setLayout(new BorderLayout());

        frame.add(new TopNavBar(currentUser, SideBar.Page.GOALS), BorderLayout.NORTH);
        frame.add(new SideBar(currentUser, SideBar.Page.GOALS), BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG);

        frame.add(contentArea, BorderLayout.CENTER);

        loadGoalsView();

        frame.setVisible(true);
    }

    // ================= VIEW =================
    private void loadGoalsView() {

        contentArea.removeAll();

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(UITheme.BG);
        root.setBorder(UITheme.pagePadding());

        JLabel title = UITheme.label("Goals", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        JLabel sub = UITheme.label("Track your savings goals", UITheme.FONT_BODY, UITheme.TEXT_SECONDARY);

        StyledButton addBtn = new StyledButton("➕ New Goal", StyledButton.Variant.PRIMARY);

        StyledButton progressBtn = new StyledButton("💰 Add Progress", StyledButton.Variant.SUCCESS);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setOpaque(false);
        btnRow.add(progressBtn);
        btnRow.add(addBtn);

        addBtn.addActionListener(e -> showAddGoal());
        progressBtn.addActionListener(e -> showAddProgress());

        List<Goal> goals = goalController.getUserGoals(currentUser);

        String[] cols = { "Goal", "Saved", "Target", "Progress", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Goal g : goals) {
            double pct = g.getProgress();
            String status = pct >= 100 ? "Completed"
                    : pct >= 75 ? "Almost there"
                            : "In Progress";

            model.addRow(new Object[] {
                    g.getName(),
                    String.format("%.2f", g.getCurrentAmount()),
                    String.format("%.2f", g.getTargetAmount()),
                    String.format("%.1f%%", pct),
                    status
            });
        }

        JTable table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);

        root.add(title);
        root.add(sub);
        root.add(Box.createVerticalStrut(10));
        root.add(btnRow);
        root.add(Box.createVerticalStrut(10));
        root.add(scroll);

        contentArea.add(root, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ================= ADD GOAL =================
    private void showAddGoal() {

        JTextField name = UITheme.styledField();
        JTextField target = UITheme.styledField();

        Object[] msg = {
                "Goal Name", name,
                "Target", target
        };

        int ok = JOptionPane.showConfirmDialog(null, msg,
                "New Goal", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            try {
                goalController.addNewGoal(
                        (int) System.currentTimeMillis(),
                        currentUser.getId(),
                        name.getText(),
                        Double.parseDouble(target.getText()));
                loadGoalsView();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input");
            }
        }
    }

    // ================= ADD PROGRESS =================
    private void showAddProgress() {

        List<Goal> goals = goalController.getUserGoals(currentUser);

        if (goals.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No goals");
            return;
        }

        String[] names = goals.stream()
                .map(Goal::getName)
                .toArray(String[]::new);

        JComboBox<String> box = new JComboBox<>(names);
        JTextField amount = UITheme.styledField();

        Object[] msg = {
                "Select Goal", box,
                "Amount", amount
        };

        int ok = JOptionPane.showConfirmDialog(null, msg,
                "Add Progress", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            try {
                Goal g = goals.get(box.getSelectedIndex());
                double val = Double.parseDouble(amount.getText());

                g.updateProgress(val);
                goalController.depositToGoal(g.getId(), val);

                loadGoalsView();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error input");
            }
        }
    }
}