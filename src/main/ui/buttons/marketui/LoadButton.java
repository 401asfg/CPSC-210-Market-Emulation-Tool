package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, loads the marketGUI's market
public class LoadButton extends MarketUIButton {
    // Initializes load button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public LoadButton(MarketGUI marketGUI) {
        super("Load", marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to load market when pressed
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: loading the marketGUI's market
            @Override
            public void actionPerformed(ActionEvent event) {
                marketGUI.doLoadMarket();
            }
        });
    }
}
