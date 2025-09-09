package view;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private final JLabel positionLabel;

    public StatusBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEtchedBorder());
        positionLabel = new JLabel("Satır: 1, Sütun: 1");
        add(positionLabel);
    }

    public void updatePosition(int line, int column) {
        positionLabel.setText(String.format("Satır: %d, Sütun: %d", line, column));
    }
}