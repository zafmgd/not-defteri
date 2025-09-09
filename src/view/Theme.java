package view;

import javax.swing.*;
import java.awt.*;

public enum Theme {
    LIGHT(
            "Açık Mod",
            new Color(240, 240, 240), // controlColor
            Color.WHITE, // backgroundColor
            new Color(50, 50, 50), // textColor
            new Color(220, 220, 220)// menuBarColor
    ),
    DARK(
            "Karanlık Mod",
            new Color(60, 63, 65), // controlColor
            new Color(45, 45, 45), // backgroundColor
            Color.WHITE, // textColor (Saf Beyaz!)
            new Color(45, 45, 45)// menuBarColor
    );

    private final String displayName;
    private final Color controlColor;
    private final Color backgroundColor;
    private final Color textColor;
    private final Color menuBarColor;

    Theme(String displayName, Color controlColor, Color backgroundColor, Color textColor, Color menuBarColor) {
        this.displayName = displayName;
        this.controlColor = controlColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.menuBarColor = menuBarColor;
    }

    public void applyTheme(JFrame frame, JTextArea textArea, JMenuBar menuBar, StatusBar statusBar) {
        frame.getContentPane().setBackground(backgroundColor);
        textArea.setBackground(backgroundColor);
        textArea.setForeground(textColor); // Metin rengini burada ayarlıyoruz!
        textArea.setCaretColor(textColor);
        menuBar.setBackground(menuBarColor);
        menuBar.setForeground(textColor);
        statusBar.setBackground(controlColor);
        statusBar.setForeground(textColor);

        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            updateMenuColors(menuBar.getMenu(i));
        }

        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void updateMenuColors(JMenu menu) {
        menu.setForeground(textColor);
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setForeground(textColor);
                if (item instanceof JMenu) {
                    updateMenuColors((JMenu) item);
                }
            }
        }
    }
}