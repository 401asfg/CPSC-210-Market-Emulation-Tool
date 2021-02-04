package ui.jframes;

import javax.swing.*;
import java.awt.*;

// A Graphical User Interface, that sets up its initial visual features upon creation
public class GUI extends JFrame {
    // Initialize gui
    // REQUIRES: defaultJFrameCloseOp must be >= 0 and < 4
    // MODIFIES: this
    // EFFECTS: calls initial methods
    public GUI(String title, int width, int height, int defaultJFrameCloseOp) {
        super();

        setupFrame(title, width, height, defaultJFrameCloseOp);
        centerOnScreen();
    }

    // Setup frame
    // REQUIRES: defaultJFrameCloseOp must be >= 0 and < 4
    // MODIFIES: this
    // EFFECTS: sets the visual parameters of the JFrame
    private void setupFrame(String title, int width, int height, int defaultJFrameCloseOp) {
        this.setTitle(title);
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(width, height));
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(defaultJFrameCloseOp);
    }

    // Centres frame on desktop
    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    private void centerOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }
}
