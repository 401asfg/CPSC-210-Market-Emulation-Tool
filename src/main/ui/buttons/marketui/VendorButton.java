package ui.buttons.marketui;

import ui.buttons.MarketUIButton;
import ui.jframes.gui.MarketGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, selects the marketGUI's vendor
public class VendorButton extends MarketUIButton {
    // Initializes vendor button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public VendorButton(MarketGUI marketGUI, String vendorName) {
        super(vendorName, marketGUI);
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to get the MarketGUI to select the vendor of the
    //          given name
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: selecting the marketGUI's vendor
            @Override
            public void actionPerformed(ActionEvent event) {
                marketGUI.doSelectVendor(getText());
            }
        });
    }
}
