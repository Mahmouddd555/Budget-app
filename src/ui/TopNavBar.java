package ui;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TopNavBar extends JPanel {

    private final User currentUser;
    private final SideBar.Page activePage;

    public TopNavBar(User user, SideBar.Page active) {
        this.currentUser = user;
        this.activePage = active;

        setPreferredSize(new Dimension(0, 52));
        setBackground(UITheme.SURFACE);
        setLayout(new BorderLayout());

        build();
    }

    private void build() {

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(UITheme.SURFACE);

        left.add(new JLabel("Masroofy"));

        JButton dashboard = btn("Dashboard");
        dashboard.addActionListener(e -> go(SideBar.Page.DASHBOARD));

        JButton goals = btn("Goals");
        goals.addActionListener(e -> go(SideBar.Page.GOALS));

        left.add(dashboard);
        left.add(goals);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(UITheme.SURFACE);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
            f.dispose();
            new LoginScreen().show();
        });

        right.add(logout);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }

    private JButton btn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(UITheme.SURFACE);
        b.setBorderPainted(false);
        return b;
    }

    private void go(SideBar.Page page) {
        JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
        f.dispose();

        switch (page) {
            case DASHBOARD -> new DashboardScreen(currentUser).show();
            case GOALS -> new GoalsScreen(currentUser).show();
            default -> {
            }
        }
    }
}