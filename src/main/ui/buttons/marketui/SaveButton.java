package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, saves the marketGUI's market
public class SaveButton extends MarketUIButton {
    // Initializes save button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public SaveButton(MarketGUI marketGUI) {
        super("Save", marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to save market when pressed
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // EFFECTS: performs the buttons action: saving the marketGUI's market
            @Override
            public void actionPerformed(ActionEvent e) {
                marketGUI.doSaveMarket();
            }
        });
    }
}
