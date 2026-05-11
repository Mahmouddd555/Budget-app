package ui;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideBar extends JPanel {

    public enum Page {
        DASHBOARD, TRANSACTIONS, BUDGETS, GOALS, SETTINGS
    }

    private final Page activePage;
    private final User currentUser;

    public SideBar(User user, Page active) {
        this.currentUser = user;
        this.activePage = active;

        setPreferredSize(new Dimension(200, 0));
        setBackground(UITheme.SIDEBAR_BG);
        setLayout(new BorderLayout());

        build();
    }

    private void build() {

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(UITheme.SIDEBAR_BG);

        menu.add(nav("🏠 Dashboard", Page.DASHBOARD));
        menu.add(nav("💸 Transactions", Page.TRANSACTIONS));
        menu.add(nav("📊 Budgets", Page.BUDGETS));
        menu.add(nav("🎯 Goals", Page.GOALS));

        JPanel bottom = new JPanel();
        bottom.setBackground(UITheme.SIDEBAR_BG);

        JLabel logout = new JLabel("🚪 Logout");
        logout.setForeground(UITheme.DANGER);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame f = (JFrame) SwingUtilities.getWindowAncestor(SideBar.this);
                f.dispose();
                new LoginScreen().show();
            }
        });

        bottom.add(logout);

        add(menu, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel nav(String text, Page page) {

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(page == activePage ? UITheme.SIDEBAR_ACTIVE : UITheme.SIDEBAR_BG);

        JLabel label = new JLabel(text);
        label.setFont(UITheme.FONT_BODY);
        label.setForeground(UITheme.TEXT_PRIMARY);

        row.add(label);

        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                navigate(page);
            }

            public void mouseEntered(MouseEvent e) {
                if (page != activePage)
                    row.setBackground(UITheme.SIDEBAR_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                row.setBackground(page == activePage ? UITheme.SIDEBAR_ACTIVE : UITheme.SIDEBAR_BG);
            }
        });

        return row;
    }

    private void navigate(Page page) {

        JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
        f.dispose();

        switch (page) {
            case DASHBOARD -> new DashboardScreen(currentUser).show();
            case TRANSACTIONS -> new AddTransactionScreen(currentUser).show();
            case BUDGETS -> new BudgetsScreen(currentUser).show();
            case GOALS -> new GoalsScreen(currentUser).show();
            case SETTINGS ->
                JOptionPane.showMessageDialog(null, "Coming soon");
        }
    }
}