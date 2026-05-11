package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StyledButton extends JButton {

    public enum Variant {
        PRIMARY, SUCCESS, DANGER, WARNING, PURPLE, GHOST
    }

    private Color baseColor;
    private Color hoverColor;
    private Color textColor;
    private boolean isGhost;

    public StyledButton(String text, Variant variant) {
        super(text);
        applyVariant(variant);
        setup();
    }

    public StyledButton(String text) {
        this(text, Variant.PRIMARY);
    }

    private void applyVariant(Variant v) {
        switch (v) {
            case PRIMARY -> {
                baseColor = UITheme.PRIMARY;
                hoverColor = UITheme.PRIMARY_DARK;
                textColor = Color.WHITE;
                isGhost = false;
            }
            case SUCCESS -> {
                baseColor = UITheme.SUCCESS;
                hoverColor = new Color(30, 140, 75);
                textColor = Color.WHITE;
                isGhost = false;
            }
            case DANGER -> {
                baseColor = UITheme.DANGER;
                hoverColor = new Color(200, 60, 60);
                textColor = Color.WHITE;
                isGhost = false;
            }
            case WARNING -> {
                baseColor = UITheme.WARNING;
                hoverColor = new Color(180, 82, 5);
                textColor = Color.WHITE;
                isGhost = false;
            }
            case PURPLE -> {
                baseColor = UITheme.PURPLE;
                hoverColor = new Color(85, 58, 200);
                textColor = Color.WHITE;
                isGhost = false;
            }
            case GHOST -> {
                baseColor = UITheme.BG;
                hoverColor = UITheme.BORDER;
                textColor = UITheme.TEXT_SECONDARY;
                isGhost = true;
            }
        }
    }

    private void setup() {
        setFont(UITheme.FONT_H3);
        setForeground(textColor);
        setBackground(baseColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(getPreferredSize().width, 38));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                setBackground(hoverColor.darker());
            }

            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (isGhost) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            g2.setColor(UITheme.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        } else {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}