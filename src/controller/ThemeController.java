package controller;

import view.MainFrame;
import view.Theme;
import view.StatusBar;

import javax.swing.*;
import java.awt.*;

public class ThemeController {
    private final JFrame frame;
    private final JTextArea textArea;
    private final JMenuBar menuBar;
    private final StatusBar statusBar;
    private Theme currentTheme = Theme.LIGHT;

    public ThemeController(JFrame frame, JTextArea textArea, JMenuBar menuBar, StatusBar statusBar) {
        this.frame = frame;
        this.textArea = textArea;
        this.menuBar = menuBar;
        this.statusBar = statusBar;
        // applyInitialTheme() çağrısı constructor'dan KALDIRILDI
    }
    
    public void applyInitialTheme() {
        currentTheme.applyTheme(frame, textArea, menuBar, statusBar);
    }

    public void toggleTheme() {
        currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        currentTheme.applyTheme(frame, textArea, menuBar, statusBar);
    }
}