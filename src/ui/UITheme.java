package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {

    // ── Palette ──────────────────────────────────────────────────────────────
    public static final Color BG = new Color(244, 246, 249);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color BORDER = new Color(229, 232, 240);

    public static final Color PRIMARY = new Color(79, 126, 248);
    public static final Color PRIMARY_SOFT = new Color(238, 242, 255);
    public static final Color PRIMARY_DARK = new Color(55, 99, 210);

    public static final Color SUCCESS = new Color(39, 174, 96);
    public static final Color SUCCESS_SOFT = new Color(232, 248, 239);

    public static final Color DANGER = new Color(240, 92, 92);
    public static final Color DANGER_SOFT = new Color(253, 236, 236);

    public static final Color WARNING = new Color(212, 102, 10);
    public static final Color WARNING_SOFT = new Color(255, 244, 234);

    public static final Color PURPLE = new Color(107, 78, 230);
    public static final Color PURPLE_SOFT = new Color(240, 238, 255);

    public static final Color TEXT_PRIMARY = new Color(26, 35, 64);
    public static final Color TEXT_SECONDARY = new Color(122, 132, 153);
    public static final Color TEXT_MUTED = new Color(176, 184, 204);

    public static final Color SIDEBAR_BG = new Color(255, 255, 255);
    public static final Color SIDEBAR_ACTIVE = new Color(238, 242, 255);
    public static final Color SIDEBAR_HOVER = new Color(244, 246, 249);

    // ── Typography ───────────────────────────────────────────────────────────
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_H3 = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 11);

    // ── Compatibility with old code ──────────────────────────────────────────
    public static final Font TITLE = FONT_TITLE;
    public static final Font NORMAL = FONT_BODY;

    // ── Borders ──────────────────────────────────────────────────────────────
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16));
    }

    public static Border fieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    public static Border focusBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
                BorderFactory.createEmptyBorder(7, 11, 7, 11));
    }

    public static Border pagePadding() {
        return BorderFactory.createEmptyBorder(20, 24, 20, 24);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(SURFACE);
        p.setBorder(cardBorder());
        return p;
    }

    // Styled text field
    public static JTextField styledField() {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        f.setFont(FONT_BODY);
        f.setBackground(SURFACE);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(PRIMARY);
        f.setBorder(fieldBorder());
        f.setOpaque(false);

        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(focusBorder());
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(fieldBorder());
            }
        });

        return f;
    }

    public static JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField();

        f.setFont(FONT_BODY);
        f.setBackground(SURFACE);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(PRIMARY);
        f.setBorder(fieldBorder());

        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(focusBorder());
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(fieldBorder());
            }
        });

        return f;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);

        c.setFont(FONT_BODY);
        c.setBackground(SURFACE);
        c.setForeground(TEXT_PRIMARY);
        c.setBorder(fieldBorder());

        return c;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);

        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_SECONDARY);

        return l;
    }

    // Stack label + field vertically
    public static JPanel fieldGroup(String labelText, JComponent field) {
        JPanel p = new JPanel();

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = fieldLabel(labelText);

        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(field);

        return p;
    }
}