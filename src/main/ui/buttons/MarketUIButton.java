package ui.buttons;

import ui.Button;
import ui.jframes.gui.MarketGUI;

// A button that can be added to the MarketGUI
public abstract class MarketUIButton extends Button {
    protected MarketGUI marketGUI;

    // Initializes button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public MarketUIButton(String text, MarketGUI marketGUI) {
        super(text);
        this.marketGUI = marketGUI;
    }
}
