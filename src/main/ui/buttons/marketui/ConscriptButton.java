package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, begins marketGUI vendor conscription process
public class ConscriptButton extends MarketUIButton {
    // Initializes conscript button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public ConscriptButton(MarketGUI marketGUI) {
        super("Conscript", marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to being vendor conscription process
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: begins marketGUI's vendor conscription process
            @Override
            public void actionPerformed(ActionEvent event) {
                marketGUI.doBeginConscription();
            }
        });
    }
}
