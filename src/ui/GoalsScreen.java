package ui;

import controller.GoalController;
import model.Goal;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GoalsScreen extends JFrame {

    private final User currentUser;
    private final GoalController goalController;

    public GoalsScreen(User user) {
        this.currentUser = user;
        this.goalController = new GoalController();
    }

    // ─── show() ──────────────────────────────────────────────────────────────

    public void show() {
        JFrame frame = new JFrame("Goals Management");
        frame.setSize(380, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton viewBtn     = new JButton("View Goals");
        JButton addBtn      = new JButton("Add Goal");
        JButton progressBtn = new JButton("Add Progress");

        viewBtn.addActionListener(e -> { frame.dispose(); viewGoals(); });
        addBtn.addActionListener(e -> { frame.dispose(); addGoal(); });
        progressBtn.addActionListener(e -> { frame.dispose(); addProgress(); });

        panel.add(viewBtn);
        panel.add(addBtn);
        panel.add(progressBtn);
        panel.add(new JLabel(""));

        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── viewGoals() ─────────────────────────────────────────────────────────

    private void viewGoals() {
        List<Goal> goals = goalController.getUserGoals(currentUser);

        String[] columns = {"Goal", "Saved", "Target", "Progress", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Goal g : goals) {
            double progress = g.getProgress();
            String status = progress >= 100 ? "✔ Completed!" : String.format("%.1f%%", progress);
            model.addRow(new Object[]{
                    g.getName(),
                    String.format("$%.2f", g.getCurrentAmount()),
                    String.format("$%.2f", g.getTargetAmount()),
                    String.format("%.1f%%", progress),
                    status
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("My Goals");
        frame.setSize(550, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { frame.dispose(); show(); });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // ─── addGoal() ───────────────────────────────────────────────────────────

    private void addGoal() {
        JFrame frame = new JFrame("Add Goal");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField   = new JTextField();
        JTextField targetField = new JTextField();

        panel.add(new JLabel("Goal name:"));
        panel.add(nameField);
        panel.add(new JLabel("Target amount ($):"));
        panel.add(targetField);
        panel.add(new JLabel(""));

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Goal name cannot be empty.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double target;
            try {
                target = Double.parseDouble(targetField.getText().trim());
                if (target <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Target must be greater than zero.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid number.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int goalId = (int) System.currentTimeMillis();
            goalController.addNewGoal(goalId, name, target);

            int choice = JOptionPane.showConfirmDialog(frame,
                    "Goal '" + name + "' created!\n\nAdd another?",
                    "Success ✔", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            if (choice == JOptionPane.YES_OPTION) addGoal();
            else show();
        });

        panel.add(saveBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    // ─── addProgress() ───────────────────────────────────────────────────────

    private void addProgress() {
        List<Goal> goals = goalController.getUserGoals(currentUser);

        if (goals.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No goals found. Please create a goal first.",
                    "No Goals", JOptionPane.INFORMATION_MESSAGE);
            show();
            return;
        }

        // بناء اسماء الـ goals للـ dropdown
        String[] goalNames = goals.stream()
                .map(g -> g.getName() + String.format(" (%.1f%% complete)", g.getProgress()))
                .toArray(String[]::new);

        JFrame frame = new JFrame("Add Progress");
        frame.setSize(380, 220);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> goalBox    = new JComboBox<>(goalNames);
        JTextField        amountField = new JTextField();

        panel.add(new JLabel("Select goal:"));
        panel.add(goalBox);
        panel.add(new JLabel("Amount to add ($):"));
        panel.add(amountField);
        panel.add(new JLabel(""));

        JButton saveBtn = new JButton("Add Progress");
        saveBtn.addActionListener(e -> {
            Goal selectedGoal = goals.get(goalBox.getSelectedIndex());

            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Amount must be greater than zero.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid number.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                selectedGoal.updateProgress(amount);
                goalController.depositToGoal(selectedGoal.getId(), amount);

                String msg = "Added $" + String.format("%.2f", amount) + " to '" + selectedGoal.getName() + "'!";
                if (selectedGoal.getProgress() >= 100) {
                    msg += "\n\n🎉 Goal completed! Congratulations!";
                }

                int choice = JOptionPane.showConfirmDialog(frame,
                        msg + "\n\nAdd more progress?",
                        "Success ✔", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
                if (choice == JOptionPane.YES_OPTION) addProgress();
                else show();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveBtn);
        frame.add(panel);
        frame.setVisible(true);
    }
}