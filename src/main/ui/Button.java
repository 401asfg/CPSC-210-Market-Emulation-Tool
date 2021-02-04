package ui;

import javax.swing.*;

// A button that can listen for actions
public abstract class Button extends JButton {
    // Initializes button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public Button(String text) {
        super(text);
        addListener();
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button
    protected abstract void addListener();
}
