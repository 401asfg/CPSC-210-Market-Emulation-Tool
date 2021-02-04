package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, expels marketGUI's selected vendor from marketGUI's market
public class ExpelButton extends MarketUIButton {
    public static final String INIT_TEXT = "Expel";

    // Initializes expel button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public ExpelButton(MarketGUI marketGUI) {
        super(INIT_TEXT, marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to expel marketGUI's selected vendor from
    //          marketGUI's market
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: expels marketGUI's selected vendor from marketGUI's market
            @Override
            public void actionPerformed(ActionEvent event) {
                marketGUI.doExpelSelectedVendor();
            }
        });
    }
}
