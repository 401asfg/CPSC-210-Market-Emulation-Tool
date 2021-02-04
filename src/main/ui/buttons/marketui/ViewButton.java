package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, views the data of marketGUI's selected vendor
public class ViewButton extends MarketUIButton {
    public static final String INIT_TEXT = "View";

    // Initializes view button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public ViewButton(MarketGUI marketGUI) {
        super(INIT_TEXT, marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to view the data of marketGUI's selected vendor
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: views the data of marketGUI's selected vendor
            @Override
            public void actionPerformed(ActionEvent event) {
                marketGUI.doViewSelectedVendor();
            }
        });
    }
}
