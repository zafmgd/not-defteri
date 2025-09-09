package controller;

import javax.swing.*;
import java.awt.*;

public class FontController {
    private final JTextArea textArea;

    public FontController(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void chooseFont() {
        Font currentFont = textArea.getFont();
        Font selectedFont = JFontChooser.showDialog(textArea, "Yazı Tipi Seç", currentFont);

        if (selectedFont != null) {
            textArea.setFont(selectedFont);
        }
    }
}

class JFontChooser {
    public static Font showDialog(Component parent, String title, Font initialFont) {
        final Font[] resultFont = new Font[1];
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        JComboBox<String> fontFamilyCombo = new JComboBox<>(fontFamilies);
        JComboBox<Integer> fontSizeCombo = new JComboBox<>(
                new Integer[] { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72 });
        JCheckBox boldCheck = new JCheckBox("Kalın");
        JCheckBox italicCheck = new JCheckBox("İtalik");
        panel.add(new JLabel("Yazı Tipi:"));
        panel.add(fontFamilyCombo);
        panel.add(new JLabel("Boyut:"));
        panel.add(fontSizeCombo);
        panel.add(boldCheck);
        panel.add(italicCheck);
        JButton okButton = new JButton("Tamam");
        JButton cancelButton = new JButton("İptal");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        if (initialFont != null) {
            fontFamilyCombo.setSelectedItem(initialFont.getFamily());
            fontSizeCombo.setSelectedItem(initialFont.getSize());
            boldCheck.setSelected(initialFont.isBold());
            italicCheck.setSelected(initialFont.isItalic());
        }
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        okButton.addActionListener(e -> {
            String family = (String) fontFamilyCombo.getSelectedItem();
            int size = (Integer) fontSizeCombo.getSelectedItem();
            int style = (boldCheck.isSelected() ? Font.BOLD : 0) | (italicCheck.isSelected() ? Font.ITALIC : 0);
            resultFont[0] = new Font(family, style, size);
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> {
            resultFont[0] = null;
            dialog.dispose();
        });
        dialog.setVisible(true);
        return resultFont[0];
    }
}