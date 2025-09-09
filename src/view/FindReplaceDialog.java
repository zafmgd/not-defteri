package view;

import controller.FindReplaceController;

import javax.swing.*;
import java.awt.*;

public class FindReplaceDialog extends JDialog {
    private final JTextField findTextField = new JTextField(20);
    private final JTextField replaceTextField = new JTextField(20);
    private final JCheckBox matchCaseCheckBox = new JCheckBox("Büyük/küçük harf duyarlı");
    private final FindReplaceController controller;

    public FindReplaceDialog(JFrame owner, FindReplaceController controller) {
        super(owner, "Bul ve Değiştir", false);
        this.controller = controller;
        this.controller.setView(this);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Bul:"), gbc);
        gbc.gridx = 1;
        panel.add(findTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Değiştir:"), gbc);
        gbc.gridx = 1;
        panel.add(replaceTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(matchCaseCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton findButton = new JButton("Bul");
        JButton replaceButton = new JButton("Değiştir");
        JButton replaceAllButton = new JButton("Tümünü Değiştir");
        JButton closeButton = new JButton("Kapat");

        buttonPanel.add(findButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        buttonPanel.add(closeButton);

        findButton.addActionListener(e -> controller.find());
        replaceButton.addActionListener(e -> controller.replace());
        replaceAllButton.addActionListener(e -> controller.replaceAll());
        closeButton.addActionListener(e -> setVisible(false));

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getFindText() { return findTextField.getText(); }
    public String getReplaceText() { return replaceTextField.getText(); }
    public boolean isMatchCase() { return matchCaseCheckBox.isSelected(); }
}