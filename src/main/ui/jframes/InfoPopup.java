package ui.jframes;

import javax.swing.*;

// A simple popup with a title and a message
public class InfoPopup extends JFrame {
    // Initialize info popup
    // MODIFIES: this
    // EFFECTS: creates a simple message dialog
    public InfoPopup(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
