package ui;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

    public StyledButton(String text, Color color) {
        super(text);

        setBackground(color);
        setForeground(Color.WHITE);
        setFont(UITheme.NORMAL);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}